
import clip.PolyLineClip;
import fill.ScanLineFill;
import fill.SeedFill;
import model.Point;
import model.PolyLine;
import model.RegularPolygon;
import rasterOper.Raster;
import rasterOper.RasterBufferedImage;
import renderOper.RendererLine;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

/**
 * trida pro kresleni na platno: zobrazeni pixelu, ovladani mysi
 * 
 * @author PGRF FIM UHK
 * @version 2017
 */
public class CanvasMouse {

	private JPanel panel;
	private BufferedImage img;

	private List<Point> pointsForLines = new ArrayList<>();

	private RegularPolygon rp;
	private Point s;
	private int n;
	private float r;
	private Point rPoint;
	private double alpha;
	private byte rpMode;

	private RendererLine renderer;
	private PolyLine polyLine = new PolyLine();

	private PolyLine clippedPolyLine = new PolyLine();
	private PolyLineClip clipper = new PolyLineClip();

	private JCheckBox chkFilling = new JCheckBox("Filling");
	private Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);

	private SeedFill sf;
	private ScanLineFill slf;
	// http://faviconer.com/
	private final byte[][] pattern = {
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,},
			{0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0,},
			{0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0,},
			{0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0,},
			{0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0,},
			{0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0,},
			{0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0,},
			{0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0,},
			{0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0,},
			{0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,},
			{0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,},
			{0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0,},
			{0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0,},
			{0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0,},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,}};

	public CanvasMouse(int width, int height) {
		JFrame frame = new JFrame();

		frame.setLayout(new BorderLayout());
		frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Raster raster = new RasterBufferedImage(img);
		renderer = new RendererLine(raster);

		PolyLine polyLineClip = new PolyLine((Color.YELLOW).getRGB());
		polyLineClip.addPoint(20,20);
		polyLineClip.addPoint(100,580);
		polyLineClip.addPoint(500,500);

		sf = new SeedFill(raster,new Color(0x0084ff));

		panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				present(g);
			}
		};
		panel.setPreferredSize(new Dimension(width, height));

		frame.add(panel);
		JToolBar tlbNorth = new JToolBar();
		tlbNorth.setFloatable(false);

		JRadioButton rbLine = new JRadioButton("Interactive line", true);
		JRadioButton rbRegPol  = new JRadioButton("Regular Polygon");
		JRadioButton rbPolClip = new JRadioButton("PolyLine clipping");
		JRadioButton rbPolFill = new JRadioButton("PolyLine filling");
		ButtonGroup rbGroup = new ButtonGroup();
		rbGroup.add(rbLine);
		rbGroup.add(rbRegPol);
		rbGroup.add(rbPolClip);
		rbGroup.add(rbPolFill);
		tlbNorth.add(rbLine);
		tlbNorth.add(rbRegPol);
		tlbNorth.add(rbPolClip);
		tlbNorth.add(rbPolFill);
		tlbNorth.addSeparator();

		JRadioButton rbSeedFill = new JRadioButton("SeedFill Color",true);
		JRadioButton rbPatternFill = new JRadioButton("Pattern SeedFill");
		ButtonGroup bgFilling = new ButtonGroup();
		bgFilling.add(rbSeedFill);
		bgFilling.add(rbPatternFill);
		chkFilling.setEnabled(false);
		rbSeedFill.setEnabled(false);
		rbPatternFill.setEnabled(false);
		tlbNorth.add(chkFilling);
		tlbNorth.add(rbSeedFill);
		tlbNorth.add(rbPatternFill);

		frame.add(tlbNorth,BorderLayout.NORTH);
		frame.pack();
		frame.setVisible(true);

		rbLine.addActionListener(e -> clearSettings());
		rbRegPol.addActionListener(e -> clearSettings());
		rbPolClip.addActionListener(e -> {
			clearSettings();
			polyLineClip.draw(renderer);
			clippedPolyLine.clearList();
		});
		rbPolFill.addActionListener(e -> {
			clearSettings();
			chkFilling.setEnabled(true);
		});
		chkFilling.addChangeListener(e -> {
			// state changed
			// more states than only enabled, disabled!
			if(chkFilling.isSelected()){
				rbSeedFill.setEnabled(true);
				rbPatternFill.setEnabled(true);
				frame.setCursor(handCursor);
			}else if(!chkFilling.isSelected()){
				clear();	// deletes filled parts of Polyline
				rbSeedFill.setEnabled(false);
				rbPatternFill.setEnabled(false);
				frame.setCursor(defaultCursor);
				drawIrregPolOnCanvas();		// deletes filled parts of Polyline
				panel.repaint();
			}
		});
		rbPatternFill.addActionListener(actionEvent -> {clear();drawIrregPolOnCanvas();panel.repaint();});
		rbSeedFill.addActionListener(actionEvent -> {clear();drawIrregPolOnCanvas();panel.repaint();});

		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(rbLine.isSelected()){
					pointsForLines.add(new Point(e.getX(),e.getY()));
				}else if(rbRegPol.isSelected()){
					if(rpMode == 0){
						n = 3;
						s = new Point(e.getX(),e.getY());
					}else if(rpMode == 1){
						rPoint = new Point(e.getX(),e.getY());
					}
					rpMode = (byte)((rpMode + 1) % 3);
				}else if((rbPolClip.isSelected() || rbPolFill.isSelected())){
					if(!chkFilling.isSelected()){	// blocks polyline drawing while in filling mode
						if(polyLine.getListSize() == 0 ) polyLine.addPoint(e.getX(),e.getY());
						if(rbPolClip.isSelected()) polyLineClip.draw(renderer);
					}else{	// chkFilling.isSelected()
					    sf.setSeed(new Point(e.getX(),e.getY()));
						if(rbSeedFill.isSelected()){
							sf.fill();
						}else{	//  pattern seed fill is selected
							sf.fill(p -> pattern[p.getY()/2 % pattern.length][p.getX()/2 % pattern[0].length] == 1);
							// division by 2 makes the pattern look bigger
						}
						panel.repaint();
					}
				}
			}
		});
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(rbLine.isSelected()){
					pointsForLines.add(new Point(e.getX(),e.getY()));
					drawSimpleLinesOnCanvas(e);	//dots
				}else if((rbPolClip.isSelected() || rbPolFill.isSelected()) && !chkFilling.isSelected()) {
					clear();
					polyLine.addPoint(e.getX(),e.getY());
					drawIrregPolOnCanvas();

					if(rbPolClip.isSelected()){
						polyLineClip.draw(renderer);
						clippedPolyLine = new PolyLine(clipper.clip(polyLine.getList(),polyLineClip.getList()), 0xffffff);
						if(clippedPolyLine.getListSize() > 2){
							slf = new ScanLineFill(raster,clippedPolyLine,Color.BLUE);
							slf.fill();
						}
					}
					panel.repaint();
				}
			}
		});
		panel.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if(rbLine.isSelected()){
					drawSimpleLinesOnCanvas(e);
				}else if((rbPolClip.isSelected() || rbPolFill.isSelected()) && !chkFilling.isSelected()){
					clear();
					drawIrregPolOnCanvas();
					if(rbPolClip.isSelected()) {
						polyLineClip.draw(renderer);
						if(clippedPolyLine.getListSize() > 2){
							slf.fill();
						}
					}
					renderer.drawLine(polyLine.getPoint(0).getX(),polyLine.getPoint(0).getY(),
							e.getX(),e.getY(),0xff0000);		// flexible parts of polyline
					if(polyLine.getListSize() > 1){
						renderer.drawLine(polyLine.getPoint(polyLine.getListSize()-1).getX(),
								polyLine.getPoint(polyLine.getListSize()-1).getY(),
								e.getX(),e.getY(),0xff0000);	// flexible parts of polyline
					}
					panel.repaint();
				}else if(rbRegPol.isSelected()  && rpMode != 0){
					drawRegPolOnCanvas(e);
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				if(rbRegPol.isSelected()){
					if(rpMode > 0 && rpMode != 3){	//optimization
						drawRegPolOnCanvas(e);
					}
				}
			}
		});
	}

	private void drawSimpleLinesOnCanvas(MouseEvent e) {
		clear();
		for(int i = 0; i< pointsForLines.size()-1; i+=2){
			renderer.drawLine(pointsForLines.get(i).getX(), pointsForLines.get(i).getY(),
					pointsForLines.get(i+1).getX(), pointsForLines.get(i+1).getY());
		}
		renderer.drawLine(pointsForLines.get(pointsForLines.size()-1).getX(),
				pointsForLines.get(pointsForLines.size()-1).getY(),e.getX(),e.getY());	//flexible line
		panel.repaint();
	}

	private void drawRegPolOnCanvas(MouseEvent e) {
		clear();
		if(rpMode == 1){
			int xFromCenter = e.getX() - s.getX();	// with sign
			int yFromCenter = e.getY() - s.getY();	// with sign
			r = (float)Math.sqrt(xFromCenter*xFromCenter + yFromCenter*yFromCenter);
			alpha = Math.atan2(e.getY()- s.getY(),e.getX()- s.getX());
			rp = new RegularPolygon(s,r,alpha,n);
			renderer.drawLine(s.getX(), s.getY(),e.getX(),e.getY(),0xff0000);  //flexible r
		}else if(rpMode == 2){
			int distance = Math.abs(rPoint.getX()-e.getX());
			n = Math.max((distance/8),3);	// Math.max((distance),3) would change n too quickly
			rp = new RegularPolygon(s,r,alpha,n);
		}
		if(rpMode > 0){
			rp.draw(renderer);
		}
		panel.repaint();
	}

	private void clearSettings() {
		clear();
		panel.repaint();
		pointsForLines.clear();
		rpMode = 0;
		polyLine = new PolyLine();

		chkFilling.setSelected(false);
		chkFilling.setEnabled(false);
	}

	private void drawIrregPolOnCanvas() {
		//clear();
		if(polyLine.getListSize() > 0){
			polyLine.draw(renderer);
		}
		//panel.repaint();
	}

	public void clear() {
		Graphics gr = img.getGraphics();
		gr.setColor(new Color(0x2f2f2f));
		gr.fillRect(0, 0, img.getWidth(), img.getHeight());
		img.getGraphics().drawString("Use mouse buttons, change mode with radio buttons, reset canvas with radio buttons",
				5, img.getHeight() - 5);
	}

	public void present(Graphics graphics) {
		graphics.drawImage(img, 0, 0, null);
	}

	public void start() {
		clear();
		panel.repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new CanvasMouse(800, 600).start());
	}
}