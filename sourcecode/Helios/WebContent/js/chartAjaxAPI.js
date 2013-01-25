BirtChart = function(contextPath) {
	if (contextPath) {
		this.contextPath = contextPath;
	} else {
		var strloc = new String(location);
		if (strloc.indexOf("http") >= 0) {
			var startIndex = strloc.indexOf("//");
			strloc = strloc.substring(startIndex + 2);
			startIndex = strloc.indexOf("/");
			strloc = strloc.substring(startIndex + 1);
			startIndex = strloc.indexOf("/");
			this.contextPath = "/" + strloc.substring(0, startIndex + 1);
		}
	}
}

BirtChart.prototype = {
	setDataXML : function(xml) {
		if (typeof xml == 'string') {
			this.dataXML = xml;
		} else if (xml instanceof ChartModel) {
			this.dataXML = xml.getXML();
		}
		this.dataURL = null;
	},
	setDataURL : function(url) {
		this.dataURL = url;
		this.dataXML = null;
	},
	setStartTime : function(millsec) {
		this.startTime = millsec;
	},
	render : function(div) {
		var url = "chart";
		var postBody;
		if (this.dataURL) {
			postBody = "dataURL=" + this.dataURL;
		} else {
			postBody = "dataXML=" + this.dataXML;
		}
		if (this.contextPath) {
			url = this.contextPath + url;
		}
		var time = this.startTime;
		var req;
		if (typeof XMLHttpRequest != "undefined") {
			req = new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}
		req.open("POST", url, true);
		req.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded");
		req.onreadystatechange = function() {
			return function() {
				if (req.readyState == 4 && req.status == 200) {
					var xmlNode = req.responseXML.getElementsByTagName("chart")[0];
					var html = xmlNode.childNodes[0].nodeValue;
					var chartDiv = (typeof div == 'string') ? document
							.getElementById(div) : div;
					chartDiv.innerHTML = html;
					if (time) {
						alert("Total time cost is: "
								+ (new Date().getTime() - time)
								+ " milliseconds.");
					}
				}
			}(div);
		};
		req.send(postBody);
	}
}

StringBuffer = function(str) {
	this.buffer = [];
	if (str) {
		this.buffer.push(str);
	}
}

StringBuffer.prototype = {
	append : function(string) {
		this.buffer.push(string);
		return this;
	},

	toString : function() {
		return this.buffer.join("");
	}
}

XMLWriter = function() {
	this.buf = new StringBuffer();
	this.bPairedFlag = true;
}

XMLWriter.prototype = {
	attribute : function(attName, attValue, defaultValue) {
		if (!defaultValue || attValue == defaultValue) {
			this.buf.append(' ' + attName + "=\"" + attValue + '\"');
		}
	},
	openTag : function(tagName) {
		if (!this.bPairedFlag) {
			this.buf.append('>');
		}
		this.bPairedFlag = false;
		this.buf.append('<').append(tagName);
	},
	closeTag : function(tagName) {
		if (!this.bPairedFlag) {
			this.buf.append("/>");
		} else {
			this.buf.append("</").append(tagName).append('>');
		}
		this.bPairedFlag = true;
	},
	text : function(value) {
		if (!this.bPairedFlag) {
			this.buf.append('>');
			this.bPairedFlag = true;
		}

		this.buf.append(value);
	},
	getXML : function() {
		return this.buf.toString();
	}
}

ChartModel = function(width, height) {
	this.type = "bar";
	this.format = "png";
	this.dimension = "2d";
	this.width = width;
	this.height = height;
	this.colorByCategory = true;
	this.showLabel = false;
	this.showLegend = true;
	this.stacked = false;	
	this.period="1101";
	this.heliosChartType = "default";
}

ChartModel.prototype = {
	setFormat : function(format) {
		this.format = format.toLowerCase();
	},
	setType : function(type) {
		this.type = type.toLowerCase();
	},
	setDimension : function(dimension) {
		this.dimension = dimension.toLowerCase();
	},
	setStacked : function(stacked) {
		this.stacked = stacked;
	},
	setColorByCategory : function(colorByCategory) {
		this.colorByCategory = colorByCategory;
	},
	setShowLegend : function(showLegend) {
		this.showLegend = showLegend;
	},
	setShowLabel : function(showLabel) {
		this.showLabel = showLabel;
	},
	setTitle : function(title) {
		this.title = title;
	},
	setScript : function(script) {
		this.script = script;
	},
	setCategories : function(categories) {
		if (isArray(categories)) {
			this.categories = categories;
		} else {
			this.categories = categories.split(",");
		}
	},
	setSeriesNames : function(seriesNames) {
		if (isArray(seriesNames)) {
			this.seriesNames = seriesNames;
		} else {
			this.seriesNames = seriesNames.split(",");
		}
	},
	setValues : function(values) {
		if (isArray(values)) {
			this.values = values;
		} else {
			this.values = values.split(",");
		}
	},
	setURLs : function(values) {
		if (isArray(values)) {
			this.urls = values;
		} else {
			this.urls = values.split(",");
		}
	},
	setTooltips : function(values) {
		if (isArray(values)) {
			this.tooltips = values;
		} else {
			this.tooltips = values.split(",");
		}
	},
	setBuildingIds : function(buildingIds) {
		if (isArray(buildingIds)) {
			this.buildingIds  = buildingIds;
		} else {
			this.buildingIds  = buildingIds.split(",");
		}
		
	},
	setPeriod : function(period) {
		this.period = period;
	},
	setHeliosChartType: function(type) {
		this.heliosChartType = type;
	},
	getXML : function() {
		var writer = new XMLWriter();
		writer.openTag("chart");
		writer.attribute("type", this.type);
		writer.attribute("format", this.format);
		writer.attribute("width", this.width);
		writer.attribute("height", this.height);
		writer.attribute("dimension", this.dimension, '2.5d');
		writer.attribute("title", this.title);
		writer.attribute("stacked", this.stacked, "false");
		writer.attribute("colorByCategory", this.colorByCategory);
		writer.attribute("showLegend", this.showLegend);
		writer.attribute("showLabel", this.showLabel);		
		writer.attribute("period",this.period);
		writer.attribute("heliosChartType",this.heliosChartType);

		var categoryLength = 0;
		if (this.categories) {
			categoryLength = this.categories.length;
			writer.openTag("categories");
			for ( var i = 0; i < categoryLength; i++) {
				writer.openTag("category");
				writer.attribute("label", this.categories[i]);
				writer.closeTag("category");
			}
			writer.closeTag("categories");
		}
		var buildingIdLength = 0;
		if (this.buildingIds) {
			buildingIdLength = this.buildingIds.length;
			writer.openTag("buildings");
			for ( var i = 0; i < buildingIdLength; i++) {
				writer.openTag("allocation");
				writer.attribute("label", this.buildingIds[i]);
				writer.closeTag("allocation");
			}
			writer.closeTag("buildings");
		}

		if (this.values && categoryLength > 0) {
			for (i = 0; i < this.values.length; i++) {
				if (i % categoryLength == 0) {
					writer.openTag("dataset");
					if (isArray(this.seriesNames)) {
						writer.attribute("name", this.seriesNames[i
								/ categoryLength]);
					}
				}

				writer.openTag("set");
				writer.attribute("value", this.values[i]);
				if (isArray(this.urls)) {
					writer.attribute("url", this.urls[i]);
				}
				if (isArray(this.tooltips)) {
					writer.attribute("tooltip", this.tooltips[i]);
				}
				writer.closeTag("set");

				if ((i + 1) % categoryLength == 0) {
					writer.closeTag("dataset");
				}
			}
		}

		if (this.script) {
			writer.openTag("script");
			writer.text(this.script);
			writer.closeTag("script");
		}

		writer.closeTag("chart");
		return writer.getXML();
	}
}

function isArray() {
	if (typeof arguments[0] == 'object') {
		var criterion = arguments[0].constructor.toString().match(/array/i);
		return (criterion != null);
	}
	return false;
}

String.prototype.endWith = function(oString) {
	var reg = new RegExp(oString + "$");
	return reg.test(this);
}