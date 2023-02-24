package renderOper;

import rasterOper.Raster;

class Renderer {
    Raster raster;

    Renderer(Raster raster){
        setRaster(raster);
    }
    private void setRaster(Raster raster){
        this.raster = raster;
    }
}