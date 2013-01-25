package org.bcje.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.helio.boomer.rap.engine.model.Period;

/**
 * 
 */

public class ChartModel {

	public static final String DIMENSION_2D = "2D"; //$NON-NLS-1$
	public static final String DIMENSION_2D_DEPTH = "2.5D"; //$NON-NLS-1$
	public static final String DIMENSION_3D = "3D"; //$NON-NLS-1$

	private String type, dimension, format;
	private  int width, height;
	private List<ChartCategory> categories = new ArrayList<ChartCategory>(4);
	private  List<ChartDataSet> datasets = new ArrayList<ChartDataSet>(4);
	private  List<String> seriesNames = new ArrayList<String>(1);
	private String title;
	private String script;
	private boolean stacked = false;
	private boolean colorByCategory = false;
	private boolean showLegend = true;
	private boolean showLabel = false;
	private boolean showTitle= true;
	
	private String startPeriod=null;
	private String endPeriod=null;
	public void setWidth(int width) {
		this.width = width;
	}


	public void setHeight(int height) {
		this.height = height;
	}

	private String heliosChartType=null;
	
	
	private List<Long> devisionIds=null;
	private List<Long> locationIds=null;
	private List<Long> buildingIds=null;
	private List<Long> buildingAllocationIds=null;
	
	private String startDate=null;
	private String endDate=null;
	
	private java.sql.Date startSqlDate=null;
	private java.sql.Date endSqlDate=null;
	
	List<Period> periods=null;
	ArrayList<ArrayList<String>> dataList=null;
	
	private String chartTitleLabel=null;
	private String domainAxisLabel=null;
	private String rangeAxisLabel=null;
	private String rangeAxisLabel2=null;
	
	public String getRangeAxisLabel2() {
		return rangeAxisLabel2;
	}


	public void setRangeAxisLabel2(String rangeAxisLabel2) {
		this.rangeAxisLabel2 = rangeAxisLabel2;
	}


	public String getChartTitleLabel() {
		return chartTitleLabel;
	}


	public void setChartTitleLabel(String chartTitleLabel) {
		this.chartTitleLabel = chartTitleLabel;
	}


	public String getDomainAxisLabel() {
		return domainAxisLabel;
	}


	public void setDomainAxisLabel(String domainAxisLabel) {
		this.domainAxisLabel = domainAxisLabel;
	}


	public String getRangeAxisLabel() {
		return rangeAxisLabel;
	}


	public void setRangeAxisLabel(String rangeAxisLabel) {
		this.rangeAxisLabel = rangeAxisLabel;
	}


	public ArrayList<ArrayList<String>> getDataList() {
		return dataList;
	}


	public void setDataList(ArrayList<ArrayList<String>> dataList) {
		this.dataList = dataList;
	}


	public ChartModel(String type, String dimension, String format,
			int width, int height) {
		this.type = type.toUpperCase();
		this.dimension = dimension.toUpperCase();
		this.format = format.toUpperCase();
		this.width = width;
		this.height = height;
	}


	public void setType(String type) {
		this.type = type;
	}


	public java.sql.Date getStartSqlDate() {
		return startSqlDate;
	}

	public void setStartSqlDate(java.sql.Date startSqlDate) {
		this.startSqlDate = startSqlDate;
	}

	public java.sql.Date getEndSqlDate() {
		return endSqlDate;
	}

	public void setEndSqlDate(java.sql.Date endSqlDate) {
		this.endSqlDate = endSqlDate;
	}

	public List<Period> getPeriods() {
		return periods;
	}

	public void setPeriods(List<Period> periods) {
		this.periods = periods;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<Long> getDevisionIds() {
		return devisionIds;
	}

	public void setDevisionIds(List<Long> devisionIds) {
		this.devisionIds = devisionIds;
	}

	public List<Long> getLocationIds() {
		return locationIds;
	}

	public void setLocationIds(List<Long> locationIds) {
		this.locationIds = locationIds;
	}

	public List<Long> getBuildingAllocationIds() {
		return buildingAllocationIds;
	}

	public void setBuildingAllocationIds(List<Long> buildingAllocationIds) {
		this.buildingAllocationIds = buildingAllocationIds;
	}

	public String getHeliosChartType() {
		return heliosChartType;
	}

	public void setHeliosChartType(String heliosChartType) {
		this.heliosChartType = heliosChartType;
	}

	public void setCategories(List<ChartCategory> categories) {
		this.categories = categories;
	}

	public void setDatasets(List<ChartDataSet> datasets) {
		this.datasets = datasets;
	}

	public void setSeriesNames(List<String> seriesNames) {
		this.seriesNames = seriesNames;
	}

	

	
	public boolean isShowTitle() {
		return showTitle;
	}

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

	public List<Long> getBuildingIds() {
		return buildingIds;
	}

	public void setBuildingIds(List<Long> buildingIds) {
		this.buildingIds = buildingIds;
	}

	

	public String getStartPeriod() {
		return startPeriod;
	}

	public void setStartPeriod(String startPeriod) {
		this.startPeriod = startPeriod;
	}

	public String getEndPeriod() {
		return endPeriod;
	}

	public void setEndPeriod(String endPeriod) {
		this.endPeriod = endPeriod;
	}

	
	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return Returns the categories.
	 */
	public List<ChartCategory> getCategories() {
		return categories;
	}

	/**
	 * @return Returns the datasets.
	 */
	public List<ChartDataSet> getDatasets() {
		return datasets;
	}
	
	/**
	 * @return the seriesNames
	 */
	public List<String> getSeriesNames() {
		return seriesNames;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return Returns the dimension.
	 */
	public String getDimension() {
		return dimension;
	}

	/**
	 * @return Returns the width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return Returns the height.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return Returns the format.
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param script
	 *            The script to set.
	 */
	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * @return Returns the script.
	 */
	public String getScript() {
		return script;
	}

	/**
	 * Stacks the multiple data if possible. The charts supporting the stacked
	 * state include: Bar, Tube, Pyramid, Cone, Line, Area.
	 * 
	 * @param stacked
	 *            The stacked to set.
	 */
	public void setStacked(boolean stacked) {
		this.stacked = stacked;
	}

	/**
	 * @return Returns the stacked.
	 */
	public boolean isStacked() {
		return stacked;
	}

	/**
	 * @param colorByCategory
	 *            The colorByCategory to set.
	 */
	public void setColorByCategory(boolean colorByCategory) {
		this.colorByCategory = colorByCategory;
	}

	/**
	 * @return Returns the colorByCategory.
	 */
	public boolean isColorByCategory() {
		return colorByCategory;
	}

	/**
	 * @param showLegend
	 *            The showLegend to set.
	 */
	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
	}

	/**
	 * @return Returns the showLegend.
	 */
	public boolean isShowLegend() {
		return showLegend;
	}

	/**
	 * @param showLabel
	 *            The showLabel to set.
	 */
	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}

	/**
	 * @return Returns the showLabel.
	 */
	public boolean isShowLabel() {
		return showLabel;
	}
}
