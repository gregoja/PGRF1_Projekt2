package rasterOper;


import java.awt.image.BufferedImage;

public class RasterBufferedImage implements Raster {
    private BufferedImage bufferedImage;

    public RasterBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    @Override
    public void drawPixel(int x, int y) {
        drawPixel(x,y,0x00ff00);
    }

    @Override
    public void drawPixel(int x, int y, int color) {
        if(x>=0 && y>=0 && x<bufferedImage.getWidth() && y<bufferedImage.getHeight()){
            bufferedImage.setRGB(x,y,color);
        }
    }

    @Override
    public int getWidth() {
        return bufferedImage.getWidth();
    }

    @Override
    public int getHeight() {
        return bufferedImage.getHeight();
    }

    @Override
    public int getPixel(int x, int y) {
        return bufferedImage.getRGB(x,y);
    }
}
