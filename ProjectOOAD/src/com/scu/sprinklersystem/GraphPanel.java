package com.scu.sprinklersystem;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class GraphPanel extends JPanel {

	private static final long serialVersionUID = 8573507988305348939L;
	//Data Members
    private int thickness = 25;
    private int labelThickness = 25;
    private Color lineColor = Color.blue;
    private Color pointColor = Color.black;
    private Color gridColor = Color.gray;
    private static final Stroke LINE_GRAPH = new BasicStroke(4);
    private int pointSize = 4;
    private int yAxisPartitions = 10;
    private List<Double> dataPoints;

    public GraphPanel(List<Double> scores) {
        this.dataPoints = scores;
    }

    //Create Graph
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        double xScale = ((double) getWidth() - (2 * thickness) - labelThickness) / (dataPoints.size() - 1);
        double yScale = ((double) getHeight() - 2 * thickness - labelThickness) / (getMaxScore() - getMinScore());

        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < dataPoints.size(); i++) {
            int x1 = (int) (i * xScale + thickness + labelThickness);
            int y1 = (int) ((getMaxScore() - dataPoints.get(i)) * yScale + thickness);
            graphPoints.add(new Point(x1, y1));
        }

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(thickness + labelThickness, thickness, getWidth() - (2 * thickness) - labelThickness, getHeight() - 2 * thickness - labelThickness);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < yAxisPartitions + 1; i++) {
            int x0 = thickness + labelThickness;
            int x1 = pointSize + thickness + labelThickness;
            int y0 = getHeight() - ((i * (getHeight() - thickness * 2 - labelThickness)) / yAxisPartitions + thickness + labelThickness);
            int y1 = y0;
            if (dataPoints.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(thickness + labelThickness + 1 + pointSize, y0, getWidth() - thickness, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinScore() + (getMaxScore() - getMinScore()) * ((i * 1.0) / yAxisPartitions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        for (int i = 0; i < dataPoints.size(); i++) {
            if (dataPoints.size() > 1) {
                int x0 = i * (getWidth() - thickness * 2 - labelThickness) / (dataPoints.size() - 1) + thickness + labelThickness;
                int x1 = x0;
                int y0 = getHeight() - thickness - labelThickness;
                int y1 = y0 - pointSize;
                if ((i % ((int) ((dataPoints.size() / 20.0)) + 1)) == 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - thickness - labelThickness - 1 - pointSize, x1, thickness);
                    g2.setColor(Color.BLACK);
                    String xLabel = i + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        // create x and y axes 
        g2.drawLine(thickness + labelThickness, getHeight() - thickness - labelThickness, thickness + labelThickness, thickness);
        g2.drawLine(thickness + labelThickness, getHeight() - thickness - labelThickness, getWidth() - thickness, getHeight() - thickness - labelThickness);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(LINE_GRAPH);
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - pointSize / 2;
            int y = graphPoints.get(i).y - pointSize / 2;
            int ovalW = pointSize;
            int ovalH = pointSize;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }
    
    private double getMinScore() {
        double minScore = Double.MAX_VALUE;
        for (Double score : dataPoints) {
            minScore = Math.min(minScore, score);
        }
        return minScore;
    }

    private double getMaxScore() {
        double maxScore = Double.MIN_VALUE;
        for (Double score : dataPoints) {
            maxScore = Math.max(maxScore, score);
        }
        return maxScore;
    }

    public void setScores(List<Double> scores) {
        this.dataPoints = scores;
        invalidate();
        this.repaint();
    }

    public List<Double> getScores() {
        return dataPoints;
    }
    
    public JComponent createAndShowGui() {
        List<Double> scores = new ArrayList<>();
        Random random = new Random();
        int maxDataPoints = 40;
        int maxScore = 10;
        JLabel title = new JLabel("Water Consumption Chart");
        for (int i = 0; i < maxDataPoints; i++) {
            scores.add((double) random.nextDouble() * maxScore);
        }
        title.setForeground(Color.black);
        GraphPanel mainPanel = new GraphPanel(scores);
        mainPanel.setPreferredSize(new Dimension(800, 600));
        mainPanel.setOpaque(false);
        mainPanel.add(title);
        return mainPanel;
    }
}