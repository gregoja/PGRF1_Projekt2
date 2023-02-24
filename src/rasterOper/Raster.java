package rasterOper;

public interface Raster {
    void drawPixel(int x, int y);
    void drawPixel(int x, int y, int color);
    int getWidth();
    int getHeight();
    int getPixel(int x, int y);
}