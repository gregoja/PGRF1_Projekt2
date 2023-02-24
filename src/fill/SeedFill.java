package fill;

import model.Point;
import rasterOper.Raster;

import java.awt.*;
import java.util.function.Predicate;

public class SeedFill implements Filler {

    private Raster raster;
    private Color color;
    private Color color2 = new Color(0x000000);  // for pattern
    private Point seed;
    private Color bgColor;
    private Predicate<Point> pattern;

    public SeedFill(Raster raster,Color color) {
        setRaster(raster);
        setColor(color);
    }

    public void setSeed(Point seed){
        this.seed = seed;
    }

    public void fill(Predicate<Point> pattern) {
        this.pattern = pattern;
        this.bgColor = new Color(raster.getPixel(seed.getX(),seed.getY()));
        if(!bgColor.equals(color) && !bgColor.equals(color2)){
            seedFill(seed.getX(),seed.getY());
        }
    }

    @Override
    public void fill() {
        fill(p -> true);
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void setRaster(Raster raster) {
        this.raster = raster;
    }

    private void seedFill(int x, int y){
        if(x >= 0 && x < raster.getWidth() && y >= 0 && y < raster.getHeight()){
            // raster.getPixel(x,y)  != color.getRGB() && raster.getPixel(x,y)  != color2.getRGB() && raster.getPixel(x,y) != boundaryColor.getRGB()
            if(raster.getPixel(x,y) == bgColor.getRGB()){
                if (pattern.test(new Point(x, y))) {
                    raster.drawPixel(x, y,color.getRGB());
                }
                else {
                    raster.drawPixel(x, y, color2.getRGB());
                }
                seedFill(x-1,y);
                seedFill(x+1,y);
                seedFill(x,y+1);
                seedFill(x,y-1);
            }
        }
    }
}