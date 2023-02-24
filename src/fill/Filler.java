package fill;

import rasterOper.Raster;
import java.awt.*;

public interface Filler {
    void fill();
    void setColor(Color color);
    void setRaster(Raster raster);
}