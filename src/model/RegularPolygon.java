package model;

import renderOper.RendererLine;

public class RegularPolygon {
    private PolyLine polyLine = new PolyLine();

    public RegularPolygon(Point s, float r, double alpha,int n){
        for (int i = 0; i<n;i++){
            polyLine.addPoint((int)(s.getX() + r*Math.cos(alpha+i*Math.PI*2/n)),(int)(s.getY() + r*Math.sin(alpha+i*Math.PI*2/n)));
        }
    }

    public void draw(RendererLine r1){
        polyLine.draw(r1);
    }
}
