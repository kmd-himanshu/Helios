 /***********************************************
*	(c) Ger Versluis 2000 version 13.21 April 28, 2008    *
*	You may use this script on non commercial sites.	          *
*	www.burmees.nl/menu			          *
*	You may remove all comments for faster loading	          *		
************************************************/
	var NoOffFirstLineMenus=4;			// Number of main menu  items
						// Colorvariables:
						// Color variables take HTML predefined color names or "#rrggbb" strings
						//For transparency make colors and border color ""
	var LowBgColor="#4189CB";			// Background color when mouse is not over
	var HighBgColor="#fd9947";			// Background color when mouse is over
	var FontLowColor="#0059a4";			// Font color when mouse is not over
	var FontHighColor="#141414";			// Font color when mouse is over
	var BorderColor="#9fc2e5";			// Border color
	var BorderWidthMain=0;			// Border width main items
	var BorderWidthSub=1;			// Border width sub items
 	var BorderBtwnMain=1;			// Border width between elements main items
	var BorderBtwnSub=1;			// Border width between elements sub items
	var FontFamily="Arial, Verdana, Helvetica, sans-serif;";	// Font family menu items
	var FontSize=12;				// Font size menu items
	var FontBold=0;				// Bold menu items 1 or 0
	var FontItalic=0;				// Italic menu items 1 or 0
	var MenuTextCentered="left";		// Item text position left, center or right
	var MenuCentered="left";			// Menu horizontal position can be: left, center, right
	var MenuVerticalCentered="top";		// Menu vertical position top, middle,bottom or static
	var ChildOverlap=.2;			// horizontal overlap child/ parent
	var ChildVerticalOverlap=.2;			// vertical overlap child/ parent
	var StartTop=0;				// Menu offset x coordinate
	var StartLeft=0;				// Menu offset y coordinate
	var VerCorrect=0;				// Multiple frames y correction
	var HorCorrect=0;				// Multiple frames x correction
	var DistFrmFrameBrdr=0;			// Distance between main menu and frame border
	var LeftPaddng=4;				// Left padding
	var TopPaddng=10;				// Top padding. If set to -1 text is vertically centered
	var FirstLineHorizontal=1;			// Number defines to which level the menu must unfold horizontal; 0 is all vertical
	var MenuFramesVertical=1;			// Frames in cols or rows 1 or 0
	var DissapearDelay=1000;			// delay before menu folds in
	var UnfoldDelay=100;			// delay before sub unfolds	
	var TakeOverBgColor=1;			// Menu frame takes over background color subitem frame
	var FirstLineFrame="";			// Frame where first level appears
	var SecLineFrame="";			// Frame where sub levels appear
	var DocTargetFrame="";			// Frame where target documents appear
	var TargetLoc="MenuPos";			// span id for relative positioning
	var MenuWrap=1;				// enables/ disables menu wrap 1 or 0
	var RightToLeft=0;				// enables/ disables right to left unfold 1 or 0
	var BottomUp=0;				// enables/ disables Bottom up unfold 1 or 0
	var UnfoldsOnClick=0;			// Level 1 unfolds onclick/ onmouseover
	var BaseHref="";				// BaseHref lets you specify the root directory for relative links. 
						// The script precedes your relative links with BaseHref
						// For instance: 
						// when your BaseHref= "http://www.MyDomain/" and a link in the menu is "subdir/MyFile.htm",
						// the script renders to: "http://www.MyDomain/subdir/MyFile.htm"
						// Can also be used when you use images in the textfields of the menu
						// "MenuX=new Array("<img src=\""+BaseHref+"MyImage\">"
						// For testing on your harddisk use syntax like: BaseHref="file:///C|/MyFiles/Homepage/"

//var Arrws=[BaseHref+"",5,10,BaseHref+"",10,5,BaseHref+"",5,10,BaseHref+"",10,5];
//var Arrws=[BaseHref+"images/tri.gif",5,7,BaseHref+"images/tridown.gif",12,7,BaseHref+"images/trileft.gif",5,10,BaseHref+"images/triup.gif",10,5];
var Arrws=[BaseHref+"",5,7,BaseHref+"",12,7,BaseHref+"",5,10,BaseHref+"",10,5];
						// Arrow source, width and height.
						// If arrow images are not needed keep source ""

	var MenuUsesFrames=0;			// MenuUsesFrames is only 0 when Main menu, submenus,
						// document targets and script are in the same frame.
						// In all other cases it must be 1

	var RememberStatus=0;			// RememberStatus: When set to 1, menu unfolds to the presetted menu item. 
						// When set to 2 only the relevant main item stays highligthed
						// The preset is done by setting a variable in the head section of the target document.
						// <head>
						//	<script type="text/javascript">var SetMenu="2_2_1";</script>
						// </head>
						// 2_2_1 represents the menu item Menu2_2_1=new Array(.......

	var BuildOnDemand=0;			// 1/0 When set to 1 the sub menus are build when the parent is moused over
	var BgImgLeftOffset=5;			// Only relevant when bg image is used as rollover
	var ScaleMenu=0;				// 1/0 When set to 0 Menu scales with browser text size setting
	var OverFormElements=0;			// Set this to 0 when the menu does not need to cover form elements.

	var HooverBold=0;				// 1 or 0
	var HooverItalic=0;				// 1 or 0
	var HooverUnderLine=0;			// 1 or 0
	var HooverTextSize=0;			// 0=off, number is font size difference on hoover
	var HooverVariant=0;			// 1 or 0

						// Below some pretty useless effects, since only IE6+ supports them
						// I provided 3 effects: MenuSlide, MenuShadow and MenuOpacity
						// If you don't need MenuSlide just leave in the line var MenuSlide="";
						// delete the other MenuSlide statements
						// In general leave the MenuSlide you need in and delete the others.
						// Above is also valid for MenuShadow and MenuOpacity
						// You can also use other effects by specifying another filter for MenuShadow and MenuOpacity.
						// You can add more filters by concanating the strings
	var MenuSlide="";
	var MenuSlide="progid:DXImageTransform.Microsoft.RevealTrans(duration=.5, transition=23)";
	var MenuSlide="progid:DXImageTransform.Microsoft.GradientWipe(duration=.5, wipeStyle=1)";

	var MenuShadow="";
	//var MenuShadow="progid:DXImageTransform.Microsoft.DropShadow(color=#B9C6D1, offX=0, offY=0, positive=0)";
	//var MenuShadow="progid:DXImageTransform.Microsoft.Shadow(color=#B9C6D1, direction=135, strength=0)";

	var MenuOpacity="";
	var MenuOpacity="progid:DXImageTransform.Microsoft.Alpha(opacity=95)";

	function BeforeStart(){return}
	function AfterBuild(){return}
	function BeforeFirstOpen(){return}
	function AfterCloseAll(){return}

// Menu tree:
// MenuX=new Array("ItemText","Link","background image",number of sub elements,height,width,"bgcolor","bghighcolor",
//	"fontcolor","fonthighcolor","bordercolor","fontfamily",fontsize,fontbold,fontitalic,"textalign","statustext");
// Color and font variables defined in the menu tree take precedence over the global variables
// Fontsize, fontbold and fontitalic are ignored when set to -1.
// For rollover images ItemText or background image format is:  "rollover?"+BaseHref+"Image1.jpg?"+BaseHref+"Image2.jpg" 


	
	
	 Menu1=new Array("rollover?"+BaseHref+"images/MenuView.gif?"+BaseHref+"images/MenuView.gif","#","images/BgMenu.gif",4,40,100,"","","#0059a4","#0059a4","","",-1,1,-1,"center","");
	 			Menu1_1=new Array("Enterprise","abc.html","",0,33,140,"#e7ecf1","#66a2d9","#0059a4","#ffffff","#66a2d9","",-1,-1,-1,"","");
				Menu1_2=new Array("Business Unit","index.jsp?MI=BUSINESS_UNIT","",0,33,140,"#e7ecf1","#66a2d9","#0059a4","#ffffff","#66a2d9","",-1,-1,-1,"","");
				Menu1_3=new Array("Plant","index.jsp?MI=PLANT_UNIT","",0,33,140,"#e7ecf1","#66a2d9","#0059a4","#ffffff","#66a2d9","",-1,-1,-1,"","");
				Menu1_4=new Array("Operations","abc.html","",0,33,140,"#e7ecf1","#66a2d9","#0059a4","#ffffff","#66a2d9","",-1,-1,-1,"","");
	
	 Menu2=new Array("rollover?"+BaseHref+"images/MenuAccounts.gif?"+BaseHref+"images/MenuAccounts.gif","#","images/BgMenu.gif",4,40,120,"","","#0059a4","#0059a4","","",-1,1,-1,"center","");
						
				Menu2_1=new Array("OS Monitoring","abc.html","",0,33,140,"#e7ecf1","#66a2d9","#0059a4","#ffffff","#66a2d9","",-1,-1,-1,"","");
				Menu2_2=new Array("Basic Monitoring","abc.html","",0,33,140,"#e7ecf1","#66a2d9","#0059a4","#ffffff","#66a2d9","",-1,-1,-1,"","");
				Menu2_3=new Array("Navigation 3","abc.html","",0,33,140,"#e7ecf1","#66a2d9","#0059a4","#ffffff","#66a2d9","",-1,-1,-1,"","");
				Menu2_4=new Array("Navigation 4","abc.html","",0,33,140,"#e7ecf1","#66a2d9","#0059a4","#ffffff","#66a2d9","",-1,-1,-1,"","");
				
				
	 Menu3=new Array("rollover?"+BaseHref+"images/MenuAdmin.gif?"+BaseHref+"images/MenuAdmin.gif","#","images/BgMenu.gif",4,40,100,"","","#0059a4","#0059a4","","",-1,1,-1,"center","");
	 
	 			Menu3_1=new Array("Navigation 1","abc.html","",0,33,140,"#e7ecf1","#66a2d9","#0059a4","#ffffff","#66a2d9","",-1,-1,-1,"","");
				Menu3_2=new Array("Navigation 2","abc.html","",0,33,140,"#e7ecf1","#66a2d9","#0059a4","#ffffff","#66a2d9","",-1,-1,-1,"","");
				Menu3_3=new Array("Navigation 3","abc.html","",0,33,140,"#e7ecf1","#66a2d9","#0059a4","#ffffff","#66a2d9","",-1,-1,-1,"","");
				Menu3_4=new Array("Navigation 4","abc.html","",0,33,140,"#e7ecf1","#66a2d9","#0059a4","#ffffff","#66a2d9","",-1,-1,-1,"","");
				
	 
	 Menu4=new Array("rollover?"+BaseHref+"images/MenuHelp.gif?"+BaseHref+"images/MenuHelp.gif","#","images/BgMenu.gif",4,40,100,"","","#0059a4","#0059a4","","",-1,1,-1,"center","");
	 
				Menu4_1=new Array("Navigation 1","abc.html","",0,33,140,"#e7ecf1","#66a2d9","#0059a4","#ffffff","#66a2d9","",-1,-1,-1,"","");
				Menu4_2=new Array("Navigation 2","abc.html","",0,33,140,"#e7ecf1","#66a2d9","#0059a4","#ffffff","#66a2d9","",-1,-1,-1,"","");
				Menu4_3=new Array("Navigation 3","abc.html","",0,33,140,"#e7ecf1","#66a2d9","#0059a4","#ffffff","#66a2d9","",-1,-1,-1,"","");
				Menu4_4=new Array("Navigation 4","abc.html","",0,33,140,"#e7ecf1","#66a2d9","#0059a4","#ffffff","#66a2d9","",-1,-1,-1,"","");
		
		
		

	
	




