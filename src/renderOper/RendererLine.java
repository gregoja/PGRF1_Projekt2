package renderOper;

import rasterOper.Raster;

public class RendererLine extends Renderer{

    public RendererLine(Raster raster){
        super(raster);
    }

    public void drawLine(int x1,int y1,int x2,int y2){
        drawLine(x1,y1,x2,y2,0x00ff00);
    }

    // DDA
    public void drawLine(int x1,int y1,int x2,int y2,int color){
        int dx = x2 - x1;
        int dy = y2 - y1;
        double k = dy/(double)dx;

        if(Math.abs(dx) > Math.abs(dy)){   // x axis
            if(x1>x2){int temporary = x1; x1=x2; x2= temporary;y1=y2;}

            double y = y1;

            for(int x=x1;x<=x2;x++){
                raster.drawPixel(x,(int)y,color);
                y+=k;
            }
        }else if(dy == 0){
            raster.drawPixel(x1,y1,color);
        }else{  // y axis
            if(y1 > y2){int temporary = y1; y1=y2;y2=temporary; x1=x2;}
            k = 1/k;
            double x = x1;

            for(int y=y1;y<=y2;y++){
                raster.drawPixel((int)x,y,color);
                x+=k;
            }
        }
    }
}