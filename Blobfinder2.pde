public class Blobfinder2 {
  
    private color samplecolor = color(0,0,0);  // the color we are looking for (if zero, face color is a default)
    private int xcr, ycr; // center of a found blob
    private int showmode = 0;  // 0 = original image, 1 = pixels with found color, 2 = cumulative sums, 3 = correlation with searched blob
    private boolean calibratesize = false;
    private int blobW, blobH;  // size of colored block to be searched for
    private int time;

    private PApplet parent;
    private int idNumber;

    private int numPixelsX,numPixelsY;
    private boolean videoready = false;

    // arrays telling for each pixel...
    private float bw[][];  // ...the amount of searched color
    private float sums[][];  // ...cumulative sums of the color within the image
    private float correl[][];  // ...how much a block centered at this pixel matches with the searched blob

    private float row[];
    private float total, maxval, mincr, maxcr;
    private float average;

    public Blobfinder2(int width, int height, PApplet parent, color samplecolor, int idNumber){
        this.parent = parent;
        this.samplecolor = samplecolor;
        this.idNumber = idNumber;

        //default blobsize
        this.blobW = (int) (0.05 * width);
        this.blobH = (int) (0.17 * height);

        this.numPixelsX = width;
        this.numPixelsY = height;
        TestScreen.myMovieColors = new int[numPixelsX * numPixelsY];
        this.bw = new float[numPixelsX][numPixelsY];
        this.sums = new float[numPixelsX][numPixelsY];
        this.correl = new float[numPixelsX][numPixelsY];
        this.row = new float[numPixelsX];

        this.time = parent.millis();

    }

    public void updateBlobCenter(){
        TestScreen ts = (TestScreen) this.parent;
        Capture cap = ts.getCam();
        if(!cap.available() && this.idNumber != 1) return;
        else {
            cap.read();
            cap.loadPixels();
            for (int j = 0; j < numPixelsY; j++) {
                for (int i = 0; i < numPixelsX; i++) {
                    myMovieColors[j*numPixelsX + i] = cap.get(i, j);
                }
            }
            calculatesums();
            videoready = true;
            if(!videoready) return;
        }
        
        if (this.idNumber == 2){
          calculatesums();
        }
        findcorrelation(blobW, blobH, 0, numPixelsX, 0, numPixelsY);
    }
    
    public Point getCurrentBlobCenter(){
      //System.out.println("getCurrentBlobCenter(): " + this);
      this.updateBlobCenter();
        return new Point(this.xcr, this.ycr);
    }
    
    public int getMyMovieColors(int index){
      return TestScreen.myMovieColors[index];
    }
    
    public void setSampleColor(color c) {
      this.samplecolor = c;
    }  
    
    public int getNumPixelsX(){
      return this.numPixelsX;
    }
    
    public int getNumPixelsY(){
      return this.numPixelsY;
    }

    void draw()
    {
        //System.out.println("samplecolor: " + this.samplecolor);
        System.out.println("draw(): " + this);
        
        findcorrelation(blobW,blobH, 0,numPixelsX, 0,numPixelsY);  // preprocessing

        if(xcr > 0) {
            this.parent.fill(255,255,50,100);
            this.parent.ellipse(xcr,ycr, 10,10);
            this.parent.stroke(255,255,50);
            this.parent.noFill();
            this.parent.beginShape();
            this.parent.vertex(xcr-blobW/2, ycr-blobH/2);
            this.parent.vertex(xcr-blobW/2, ycr+blobH/2);
            this.parent.vertex(xcr+blobW/2, ycr+blobH/2);
            this.parent.vertex(xcr+blobW/2, ycr-blobH/2);
            this.parent.endShape(this.parent.CLOSE);
            this.parent.noStroke();
        }
            // this part tries to adaptively optimize the blob size - still experimental but may be developed...

        videoready = false;
        speedmeter();
    }

    void speedmeter()
    // visually show the frame time in tens of milliseconds, to check efficiency
    {
        int now = this.parent.millis();
        int frametime = now - time;
        time = now;
        if(frametime < 0) frametime += 1000;
        int tens = frametime / 10;
        // println(tens+" "+frametime);
        for(int i=0;i<tens;i++) {
            this.parent.noStroke();
            this.parent.fill(0,255,0);             // <  50ms / frame is optimal
            if(i > 5) this.parent.fill(255,255,0);
            if(i > 10) this.parent.fill(255,0,0);  // > 100ms / frame gets bad
            this.parent.rect(this.parent.width-5,20*i, 5,5);
        }
    }

    void keyPressed()
    {
        if(this.parent.key == '0') showmode = 0;
        if(this.parent.key == '1') showmode = 1;
        if(this.parent.key == '2') showmode = 2;
        if(this.parent.key == '3') showmode = 3;
        if(this.parent.key == 'c') calibratesize = true;
    }

    // preprocerssing for efficient correlation later
    void calculatesums()
    {
        for(int j=0;j<numPixelsY;j++)
            for(int i=0;i<numPixelsX;i++)
            {
                // first find the color of interest
                // encode its amount in this pixel into 'value'
                int c = myMovieColors[j*numPixelsX + i];
                float value;
                float b = this.parent.brightness(c);
                float h = this.parent.hue(c);
                float s = this.parent.saturation(c);

                float bs = parent.brightness(samplecolor);
                float hs = parent.hue(samplecolor);
                float ss = parent.saturation(samplecolor);
                float toler = 50;
                // look if the pixel color is close enough to the sample color; tolerance of the comparison can be tuned
                if(b > bs-toler && h > hs-toler && h < hs+toler &&
                        s > ss-toler && s < ss+toler) value = b; else value = 0;

                // value = b;  // if looking just for brightness
                bw[i][j] = value;

                // then make acculumated sums along rows
                // NOTE: sums[i][j] tells the total amount of right color (= value) within rectangle (0,0, i,j)
                if(i == 0) row[i] = value; else row[i] = row[i-1] + value;
                if(j == 0) sums[i][j] = row[i]; else sums[i][j] = sums[i][j-1] + row[i];
            }
        // total amount of value in the picture
        total = sums[numPixelsX-1][numPixelsY-1];
    }


    void findcorrelation(int w, int h, int x1, int x2, int y1, int y2)
    // search within limits (x1,x2, y1,y2) the maximal match with a block of size w x h
    // encode result to array correl[][] 
    {
        average = total / (float)(numPixelsX * numPixelsY);
        mincr = 100000;
        maxcr = -mincr;
        int blocksize = w * h;
        xcr = ycr = -1;

        for(int i=x1;i<x2;i++)
            for(int j=y1;j<y2;j++) {
                float c = (correlate(i,j, w,h) / blocksize) - average;
                correl[i][j] = c;
                if(c < mincr) mincr = c;
                if(c > maxcr) { maxcr = c; xcr = i; ycr = j; }
            }  
    }

    float correlate(int cx, int cy, int w, int h)
    // this implements correlation very efficiently!
    {
        // looking for a block centered at (cx,cy), use (x,y) for left-up corner of the block
        int x = cx - w/2;
        int y = cy - h/2;
        if(x < 0 || y < 0 || x > numPixelsX-1-w || y > numPixelsY-1-h) return 0;
        float c = (sums[x][y] + sums[x+w][y+h] - sums[x+w][y] - sums[x][y+h]);
        return c;
    }

    void optimize()
    // trying to make blobsize adaptive - doesn't work properly yet...
    {
        if(blobW < 50 || blobW > 300) return;
        int blocksize;
        int tryX = (int)(blobW * 1.05);
        int tryY = (int)(blobH * 1.05);
        blocksize = tryX * tryY;
        float c = correlate(xcr,ycr, tryX,tryY) / blocksize - average;
        if(c > maxcr) { blobW = tryX; blobH = tryY; return; }
        tryX = (int)(blobW * 0.95);
        tryY = (int)(blobH * 0.95);
        blocksize = tryX * tryY;
        c = correlate(xcr,ycr, tryX,tryY) / blocksize - average;
        if(c > maxcr) { blobW = tryX; blobH = tryY; }
    }
}

