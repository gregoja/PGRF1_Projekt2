package fill;

import model.Point;
import model.PolyLine;
import rasterOper.Raster;
import renderOper.RendererLine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


public class ScanLineFill implements Filler {

    private List<Line> lines = new ArrayList<>();
    private PolyLine polyLine;
    private int yMin;
    private int yMax;
    private Raster raster;
    private Color color;

    public ScanLineFill(Raster raster,PolyLine polyLine,Color color) {
        setRaster(raster);
        setBounds(polyLine);
        setColor(color);
    }

    private void setBounds(PolyLine polyLine){
        this.polyLine = new PolyLine(polyLine);
        fillArrayListLines();
        findMinMax();
    }

    private void fillArrayListLines() {
        for(int i = 0; i < polyLine.getList().size();i++){
            Line newLine = new Line(polyLine.getList().get(i),polyLine.getList().get((i+1)%polyLine.getList().size()));
            if(!newLine.isHorizontal()){    // horizontal lines won't be added to List
                newLine.setOrientation();   // orientation : top -> bottom , y axis +
                lines.add(newLine);
            }
        }
    }

    private void findMinMax(){
        yMin = polyLine.getList().get(0).getY();
        yMax = polyLine.getList().get(0).getY();
        for(Point point : polyLine.getList()){
            if(point.getY() < yMin){
                yMin = point.getY();
            }
            if(point.getY() > yMax){
                yMax = point.getY();
            }
        }
    }

    @Override
    public void fill() {
        for(int y = yMin; y <= yMax;y++){
            List<Integer> intersections = new ArrayList<>();
            for (Line line : lines) {
                if( line.isIntersection(y)){
                    intersections.add(line.getIntersection(y));
                }
            }
            // Collections.sort();
            // Insertion sort
            for (int i = 0; i < intersections.size()-1;i++){
                int j = i+1;
                int temporary = intersections.get(j);
                while (j > 0 && temporary < intersections.get(j-1)){
                    intersections.set(j,intersections.get(j-1));
                    j--;
                }
                intersections.set(j,temporary);
            }
            for(int i = 0; i < intersections.size(); i+=2){
                for (int x = intersections.get(i); x<= intersections.get(i+1);x++) {
                    raster.drawPixel(x,y,color.getRGB());
                }
            }
        }
        polyLine.draw(new RendererLine(raster));
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void setRaster(Raster raster) {
        this.raster = raster;
    }



    private static class Line{
        private Point a,b;
        // x = f(y)
        private double k;
        private double q;

        private Line(Point a, Point b) {
            this.a = a;
            this.b = b;
            if (a.getY() != b.getY()) {
                //dx/dy
                k = (b.getX() - a.getX()) /(double) (b.getY() - a.getY());
                q = a.getX() - k * a.getY();
            }
        }

        private boolean isIntersection(int y){
            return (a.getY() <= y && b.getY() > y);
        }

        private boolean isHorizontal(){
            return (a.getY() == b.getY());
        }

        private int getIntersection(int y){
            return (int)Math.round(k*y+q);
        }

        private void setOrientation(){
            if(a.getY() > b.getY()){
                Point temporary = a;
                a = b;
                b = temporary;
            }
        }
    }
}