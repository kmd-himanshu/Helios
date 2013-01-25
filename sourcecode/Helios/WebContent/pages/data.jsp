<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<f:view><html>
<head>

</head>
<body>
<center>
<br><br><br>
<h:dataTable id="dt1" value="#{tableBean.perInfoAll}" var="item" bgcolor="#F1F1F1" border="10" cellpadding="5" cellspacing="3"  rows="4" width="50%" dir="LTR" frame="hsides" rules="all" summary="This is a JSF code to create dataTable." >

<f:facet name="header">
        <h:outputText value="This is 'dataTable' demo" />
</f:facet> 

<h:column>
        <f:facet name="header">
        <h:outputText value="First Name" />
        </f:facet> 
             <h:outputText style=""  value="#{item.firstName}" ></h:outputText>
</h:column>

<h:column>
        <f:facet name="header">
        <h:outputText value="Last Name"/>
        </f:facet> 
             <h:outputText  value="#{item.lastName}"></h:outputText>
</h:column>

<h:column>
        <f:facet name="header">
        <h:outputText value="Username"/>
        </f:facet> 
             <h:outputText value="#{item.uname}"></h:outputText>
</h:column>

<f:facet name="footer">
        <h:outputText value="The End" />
</f:facet> 

</h:dataTable><br>


</center>
</body></html></f:view>