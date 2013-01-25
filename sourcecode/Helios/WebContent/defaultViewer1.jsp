<%-- <%@page import="org.bcje.servlets.LabelGenerator"%>
<%@page import="org.jfree.chart.labels.StandardCategoryToolTipGenerator"%>
<%@page import="org.jfree.chart.urls.StandardCategoryURLGenerator"%>
<%@page import="org.jfree.chart.axis.CategoryAxis"%>
<%@page import="org.jfree.chart.axis.CategoryLabelPositions"%>
<%@page import="org.jfree.chart.labels.ItemLabelAnchor"%>
<%@page import="org.jfree.chart.labels.ItemLabelPosition"%>
<%@page import="org.jfree.chart.renderer.category.BarRenderer"%>
<%@page import="org.jfree.chart.axis.NumberAxis"%>
<%@page import="org.jfree.ui.Layer"%>
<%@page import="java.awt.Color"%>
<%@page import="org.jfree.ui.TextAnchor"%>
<%@page import="org.jfree.ui.RectangleAnchor"%>
<%@page import="java.awt.Font"%>
<%@page import="org.jfree.chart.plot.PlotOrientation"%>
<%@page import="org.jfree.chart.plot.IntervalMarker"%>
<%@page import="org.jfree.chart.plot.CategoryPlot"%>
<%@page import="org.jfree.chart.*"%>
<%@page import="org.jfree.chart.ChartFactory"%>
<%@page import="org.jfree.data.general.DatasetUtilities"%>
<%@page import="org.jfree.chart.JFreeChart"%>
<%@page import="org.jfree.data.category.CategoryDataset"%>
<%
final double[][] data = new double[][] { { 210, 300, 320, 265 }, { 200, 304, 201, 201 } };
String[] seriesNames = new String[] { "Actual", "Budget" };
String[] categoryNames = new String[] { "Loan", "Trade", "Cash", "Treasury" };
CategoryDataset dataset = DatasetUtilities.createCategoryDataset(seriesNames, categoryNames, data);

JFreeChart jfreechart1 = ChartFactory.createBarChart("Bar Chart Demo", "", " ", dataset, PlotOrientation.VERTICAL, true, true, true);
CategoryPlot categoryplot = (CategoryPlot) jfreechart1.getPlot();
categoryplot.setRangePannable(true);
IntervalMarker intervalmarker = new IntervalMarker(4.5D, 7.5D);
intervalmarker.setLabel("Target Range");
intervalmarker.setLabelFont(new Font("SansSerif", 2, 11));
intervalmarker.setLabelAnchor(RectangleAnchor.LEFT);
intervalmarker.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
intervalmarker.setPaint(new Color(222, 222, 255, 128));
categoryplot.addRangeMarker(intervalmarker, Layer.BACKGROUND);
NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
BarRenderer barrenderer = (BarRenderer) categoryplot.getRenderer();
barrenderer.setDrawBarOutline(false);
barrenderer.setItemMargin(0.10000000000000001D);
barrenderer.setBaseItemLabelGenerator(new LabelGenerator());
barrenderer.setBaseItemLabelsVisible(true);
ItemLabelPosition itemlabelposition = new ItemLabelPosition(ItemLabelAnchor.INSIDE12, TextAnchor.CENTER_RIGHT, TextAnchor.CENTER_RIGHT,
-1.5707963267948966D);
barrenderer.setBasePositiveItemLabelPosition(itemlabelposition);
ItemLabelPosition itemlabelposition1 = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER_LEFT, TextAnchor.CENTER_LEFT,
-1.5707963267948966D);
barrenderer.setPositiveItemLabelPositionFallback(itemlabelposition1);
CategoryAxis categoryaxis = categoryplot.getDomainAxis();
categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
barrenderer.setBaseItemURLGenerator(new StandardCategoryURLGenerator("www.google.com", seriesNames[1], categoryNames[2]));
String url = new StandardCategoryURLGenerator("www.google.com", seriesNames[1], categoryNames[2]).generateURL(dataset, 1, 1);
barrenderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
String tooltip = new StandardCategoryToolTipGenerator().generateToolTip(dataset, 1, 1);
ChartUtilities.writeChartAsJPEG(response.getOutputStream(), jfreechart1, 400, 400);
response.getOutputStream().close();

%> --%>
<%@page import="org.bcje.servlets.DualAxisChart"%>
<%@page import="org.jfree.chart.renderer.category.CategoryItemRenderer"%>
<%@page import="org.jfree.chart.plot.CategoryPlot"%>
<%@page import="java.awt.Color"%>
<%@page import="org.jfree.chart.plot.PlotOrientation"%>
<%@page import="org.jfree.chart.ChartFactory"%>
<%@page import="org.jfree.data.category.DefaultCategoryDataset"%>
<%@page import="org.jfree.chart.ChartUtilities"%>
<%@page import="org.jfree.chart.urls.StandardCategoryURLGenerator"%>
<%@page import="org.jfree.chart.labels.StandardCategoryToolTipGenerator"%>
<%@page import="org.jfree.chart.JFreeChart"%>
<%@page import="org.jfree.chart.ChartRenderingInfo"%>
<%
// get ImageMap
JFreeChart chart=new DualAxisChart().createChart(); 
ChartRenderingInfo info = new ChartRenderingInfo(); 
// populate the info
chart.createBufferedImage(350, 300, info); 
String imageMap = ChartUtilities.getImageMap( "map", info ); 
session.setAttribute( "defaultViewer1"+"chart", chart ); 



%>
<%= imageMap%>

// include the call for the image
<IMG src="chartViewer?page=Cases_handled_per_period&width=250&height=200" style="border:1px solid red" usemap="#map">