package model;

import renderOper.RendererLine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PolyLine {
    private List<Point> list;
    private int color;

    public PolyLine(PolyLine polyline) {
        this.color = polyline.color;
        this.list = polyline.getList();
    }

    public PolyLine() {
        this.color = 0x00ff00;
        this.list = new ArrayList<>();
    }

    public PolyLine(int color) {
        this.color = color;
        this.list = new ArrayList<>();
    }

    public PolyLine(List<Point> list, int color) {
        this.list = list;
        this.color = color;
    }

    public void addPoint(Point point){
        list.add(point);
    }

    public void addPoint(int x, int y){
        list.add(new Point(x,y));
    }

    public List<Point> getList(){
        return Collections.unmodifiableList(list);
    }

    public void clearList(){
        list.clear();
    }

    public void draw(RendererLine rendererLine){
        for(int i = 0; i< list.size();i++){
          rendererLine.drawLine(list.get(i).getX(),list.get(i).getY(),list.get((i+1)%list.size()).getX(),list.get((i+1)%list.size()).getY(),color);
        }

    }

    public int getListSize() {
        return list.size();
    }

    public Point getPoint(int index) {
        return list.get(index);
    }
}
