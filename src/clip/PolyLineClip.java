package clip;

import model.Point;

import java.util.ArrayList;
import java.util.List;

public class PolyLineClip {


    public List<Point> clip(List<Point> polyLineList, List<Point> clipPolylineList){
        if(clipPolylineList.size() < 2){return polyLineList;}
        List <Point> clippedPolyLineList = polyLineList;
        for(int i = 0; i< clipPolylineList.size();i++){
            if(clippedPolyLineList.size() < 2) {return clippedPolyLineList;}
            Edge edge = new Edge(clipPolylineList.get(i), clipPolylineList.get((i+1)%clipPolylineList.size()));
            clippedPolyLineList = clipByEdge(edge,clippedPolyLineList);
        }
        return clippedPolyLineList;
    }

    private List<Point> clipByEdge(Edge edge, List<Point> polyLineList) {
        List<Point> out = new ArrayList<>();
        Point v1 = polyLineList.get(polyLineList.size()-1);
        for (Point v2 : polyLineList) {
            if(edge.inside(v2)){
                if(!edge.inside(v1)){
                    out.add(edge.getIntersection(v1,v2));
                }
                out.add(v2);
            }else if(edge.inside(v1)){
                out.add(edge.getIntersection(v1,v2));
            }
            v1 = v2;
        }
        return out;
    }


    private static class Edge{
        private Point a;
        private Point b;

        private Edge(Point a, Point b) {
            this.a = a;
            this.b = b;
        }

        private boolean inside(Point v2) {
            Vector2D t = new Vector2D(b.getX()- a.getX(), b.getY()- a.getY());
            Vector2D n = new Vector2D(-t.y,t.x);
            Vector2D v = new Vector2D(v2.getX() - a.getX(),v2.getY()- a.getY());
            return ((v.x * n.x) + (v.y * n.y) < 0);
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "p1=" + a +
                    ", p2=" + b +
                    '}';
        }

        private Point getIntersection(Point v1, Point v2) {
            /*int x1 = v1.getX();
            int x2 = v2.getX();
            int x3 = a.getX();
            int x4 = b.getX();
            int y1 = v1.getY();
            int y2 = v2.getY();
            int y3 = a.getY();
            int y4 = b.getY();*/

            double denominator = ((v1.getX() - v2.getX()) * (a.getY() - b.getY()) - (v1.getY() - v2.getY()) * (a.getX() - b.getX()));

            int x0 = (int)Math.round(((v1.getX() * v2.getY() - v2.getX() * v1.getY()) * (a.getX() - b.getX())
                    - (a.getX() * b.getY() - b.getX() * a.getY()) * (v1.getX() - v2.getX()))/denominator);
            int y0 = (int)Math.round(((v1.getX() * v2.getY() - v2.getX() * v1.getY()) * (a.getY() - b.getY())
                    - (a.getX() * b.getY() - b.getX() * a.getY()) * (v1.getY() - v2.getY()))/denominator);
            return new Point (x0,y0);
        }

        //transforms.Vec2D not required, yet.
        private static class Vector2D{
            private final int x;
            private final int y;

            private Vector2D(int x, int y) {
                this.x = x;
                this.y = y;
            }
        }
    }
}
