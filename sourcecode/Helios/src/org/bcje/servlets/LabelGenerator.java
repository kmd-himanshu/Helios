package org.bcje.servlets;

import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.data.category.CategoryDataset;

public class LabelGenerator extends StandardCategoryItemLabelGenerator {

public String generateLabel(CategoryDataset categorydataset, int i, int j) {
return categorydataset.getRowKey(i).toString();
}

public LabelGenerator() {
}
}
