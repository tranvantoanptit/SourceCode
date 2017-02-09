(function(f){typeof module==="object"&&module.exports?module.exports=f:f(Highcharts)})(function(f){var s=f.win,j=s.document,B=f.Chart,u=f.addEvent,C=f.removeEvent,D=f.fireEvent,r=f.createElement,t=f.discardElement,w=f.css,l=f.merge,m=f.each,q=f.extend,F=f.splat,G=Math.max,H=f.isTouchDevice,I=f.Renderer.prototype.symbols,z=f.getOptions(),A;q(z.lang,{printChart:"Print chart",downloadPNG:"Download PNG image",downloadJPEG:"Download JPEG image",downloadPDF:"Download PDF document",downloadSVG:"Download SVG vector image",
contextButtonTitle:"Chart context menu"});z.navigation={menuStyle:{border:"1px solid #A0A0A0",background:"#FFFFFF",padding:"5px 0"},menuItemStyle:{padding:"0 10px",background:"none",color:"#303030",fontSize:H?"14px":"11px"},menuItemHoverStyle:{background:"#4572A5",color:"#FFFFFF"},buttonOptions:{symbolFill:"#E0E0E0",symbolSize:14,symbolStroke:"#666",symbolStrokeWidth:3,symbolX:12.5,symbolY:10.5,align:"right",buttonSpacing:3,height:22,theme:{fill:"white",stroke:"none"},verticalAlign:"top",width:24}};
z.exporting={type:"image/png",url:"https://export.highcharts.com/",printMaxWidth:780,scale:2,buttons:{contextButton:{menuClassName:"highcharts-contextmenu",symbol:"menu",_titleKey:"contextButtonTitle",menuItems:[{textKey:"printChart",onclick:function(){this.print()}},{separator:!0},{textKey:"downloadPNG",onclick:function(){this.exportChart()}},{textKey:"downloadJPEG",onclick:function(){this.exportChart({type:"image/jpeg"})}},{textKey:"downloadPDF",onclick:function(){this.exportChart({type:"application/pdf"})}},
{textKey:"downloadSVG",onclick:function(){this.exportChart({type:"image/svg+xml"})}}]}}};f.post=function(a,b,e){var c,a=r("form",l({method:"post",action:a,enctype:"multipart/form-data"},e),{display:"none"},j.body);for(c in b)r("input",{type:"hidden",name:c,value:b[c]},null,a);a.submit();t(a)};q(B.prototype,{sanitizeSVG:function(a){return a.replace(/zIndex="[^"]+"/g,"").replace(/isShadow="[^"]+"/g,"").replace(/symbolName="[^"]+"/g,"").replace(/jQuery[0-9]+="[^"]+"/g,"").replace(/url\([^#]+#/g,"url(#").replace(/<svg /,
'<svg xmlns:xlink="http://www.w3.org/1999/xlink" ').replace(/ (NS[0-9]+\:)?href=/g," xlink:href=").replace(/\n/," ").replace(/<\/svg>.*?$/,"</svg>").replace(/(fill|stroke)="rgba\(([ 0-9]+,[ 0-9]+,[ 0-9]+),([ 0-9\.]+)\)"/g,'$1="rgb($2)" $1-opacity="$3"').replace(/&nbsp;/g,"\u00a0").replace(/&shy;/g,"\u00ad").replace(/<IMG /g,"<image ").replace(/<(\/?)TITLE>/g,"<$1title>").replace(/height=([^" ]+)/g,'height="$1"').replace(/width=([^" ]+)/g,'width="$1"').replace(/hc-svg-href="([^"]+)">/g,'xlink:href="$1"/>').replace(/ id=([^" >]+)/g,
' id="$1"').replace(/class=([^" >]+)/g,'class="$1"').replace(/ transform /g," ").replace(/:(path|rect)/g,"$1").replace(/style="([^"]+)"/g,function(a){return a.toLowerCase()})},getChartHTML:function(){return this.container.innerHTML},getSVG:function(a){var b=this,e,c,g,k,h,d=l(b.options,a),n=d.exporting.allowHTML;if(!j.createElementNS)j.createElementNS=function(a,b){return j.createElement(b)};c=r("div",null,{position:"absolute",top:"-9999em",width:b.chartWidth+"px",height:b.chartHeight+"px"},j.body);
g=b.renderTo.style.width;h=b.renderTo.style.height;g=d.exporting.sourceWidth||d.chart.width||/px$/.test(g)&&parseInt(g,10)||600;h=d.exporting.sourceHeight||d.chart.height||/px$/.test(h)&&parseInt(h,10)||400;q(d.chart,{animation:!1,renderTo:c,forExport:!0,renderer:"SVGRenderer",width:g,height:h});d.exporting.enabled=!1;delete d.data;d.series=[];m(b.series,function(a){k=l(a.userOptions,{animation:!1,enableMouseTracking:!1,showCheckbox:!1,visible:a.visible});k.isInternal||d.series.push(k)});a&&m(["xAxis",
"yAxis"],function(b){m(F(a[b]),function(a,c){d[b][c]=l(d[b][c],a)})});e=new f.Chart(d,b.callback);m(["xAxis","yAxis"],function(a){m(b[a],function(b,c){var d=e[a][c],f=b.getExtremes(),g=f.userMin,f=f.userMax;d&&(g!==void 0||f!==void 0)&&d.setExtremes(g,f,!0,!1)})});g=e.getChartHTML();d=null;e.destroy();t(c);if(n&&(c=g.match(/<\/svg>(.*?$)/)))c='<foreignObject x="0" y="0" width="200" height="200"><body xmlns="http://www.w3.org/1999/xhtml">'+c[1]+"</body></foreignObject>",g=g.replace("</svg>",c+"</svg>");
g=this.sanitizeSVG(g);return g=g.replace(/(url\(#highcharts-[0-9]+)&quot;/g,"$1").replace(/&quot;/g,"'")},getSVGForExport:function(a,b){var e=this.options.exporting;return this.getSVG(l({chart:{borderRadius:0}},e.chartOptions,b,{exporting:{sourceWidth:a&&a.sourceWidth||e.sourceWidth,sourceHeight:a&&a.sourceHeight||e.sourceHeight}}))},exportChart:function(a,b){var e=this.getSVGForExport(a,b),a=l(this.options.exporting,a);f.post(a.url,{filename:a.filename||"chart",type:a.type,width:a.width||0,scale:a.scale,
svg:e},a.formAttributes)},print:function(){var a=this,b=a.container,e=[],c=b.parentNode,f=j.body,k=f.childNodes,h=a.options.exporting.printMaxWidth,d,n;if(!a.isPrinting){a.isPrinting=!0;a.pointer.reset(null,0);D(a,"beforePrint");if(n=h&&a.chartWidth>h)d=[a.options.chart.width,void 0,!1],a.setSize(h,void 0,!1);m(k,function(a,b){if(a.nodeType===1)e[b]=a.style.display,a.style.display="none"});f.appendChild(b);s.focus();s.print();setTimeout(function(){c.appendChild(b);m(k,function(a,b){if(a.nodeType===
1)a.style.display=e[b]});a.isPrinting=!1;n&&a.setSize.apply(a,d);D(a,"afterPrint")},1E3)}},contextMenu:function(a,b,e,c,f,k,h){var d=this,n=d.options.navigation,l=n.menuItemStyle,o=d.chartWidth,p=d.chartHeight,E="cache-"+a,i=d[E],v=G(f,k),x,y,s,t=function(b){d.pointer.inClass(b.target,a)||y()};if(!i)d[E]=i=r("div",{className:a},{position:"absolute",zIndex:1E3,padding:v+"px"},d.container),x=r("div",null,q({MozBoxShadow:"3px 3px 10px #888",WebkitBoxShadow:"3px 3px 10px #888",boxShadow:"3px 3px 10px #888"},
n.menuStyle),i),y=function(){w(i,{display:"none"});h&&h.setState(0);d.openMenu=!1},u(i,"mouseleave",function(){s=setTimeout(y,500)}),u(i,"mouseenter",function(){clearTimeout(s)}),u(j,"mouseup",t),u(d,"destroy",function(){C(j,"mouseup",t)}),m(b,function(a){if(a){var b=a.separator?r("hr",null,null,x):r("div",{onmouseover:function(){w(this,n.menuItemHoverStyle)},onmouseout:function(){w(this,l)},onclick:function(b){b&&b.stopPropagation();y();a.onclick&&a.onclick.apply(d,arguments)},innerHTML:a.text||
d.options.lang[a.textKey]},q({cursor:"pointer"},l),x);d.exportDivElements.push(b)}}),d.exportDivElements.push(x,i),d.exportMenuWidth=i.offsetWidth,d.exportMenuHeight=i.offsetHeight;b={display:"block"};e+d.exportMenuWidth>o?b.right=o-e-f-v+"px":b.left=e-v+"px";c+k+d.exportMenuHeight>p&&h.alignOptions.verticalAlign!=="top"?b.bottom=p-c-v+"px":b.top=c+k-v+"px";w(i,b);d.openMenu=!0},addButton:function(a){var b=this,e=b.renderer,c=l(b.options.navigation.buttonOptions,a),g=c.onclick,k=c.menuItems,h,d,n=
{stroke:c.symbolStroke,fill:c.symbolFill},j=c.symbolSize||12;if(!b.btnCount)b.btnCount=0;if(!b.exportDivElements)b.exportDivElements=[],b.exportSVGElements=[];if(c.enabled!==!1){var o=c.theme,p=o.states,m=p&&p.hover,p=p&&p.select,i;delete o.states;g?i=function(a){a.stopPropagation();g.call(b,a)}:k&&(i=function(){b.contextMenu(d.menuClassName,k,d.translateX,d.translateY,d.width,d.height,d);d.setState(2)});c.text&&c.symbol?o.paddingLeft=f.pick(o.paddingLeft,25):c.text||q(o,{width:c.width,height:c.height,
padding:0});d=e.button(c.text,0,0,i,o,m,p).attr({title:b.options.lang[c._titleKey],"stroke-linecap":"round",zIndex:3});d.menuClassName=a.menuClassName||"highcharts-menu-"+b.btnCount++;c.symbol&&(h=e.symbol(c.symbol,c.symbolX-j/2,c.symbolY-j/2,j,j).attr(q(n,{"stroke-width":c.symbolStrokeWidth||1,zIndex:1})).add(d));d.add().align(q(c,{width:d.width,x:f.pick(c.x,A)}),!0,"spacingBox");A+=(d.width+c.buttonSpacing)*(c.align==="right"?-1:1);b.exportSVGElements.push(d,h)}},destroyExport:function(a){var a=
a.target,b,e;for(b=0;b<a.exportSVGElements.length;b++)if(e=a.exportSVGElements[b])e.onclick=e.ontouchstart=null,a.exportSVGElements[b]=e.destroy();for(b=0;b<a.exportDivElements.length;b++)e=a.exportDivElements[b],C(e,"mouseleave"),a.exportDivElements[b]=e.onmouseout=e.onmouseover=e.ontouchstart=e.onclick=null,t(e)}});I.menu=function(a,b,e,c){return["M",a,b+2.5,"L",a+e,b+2.5,"M",a,b+c/2+0.5,"L",a+e,b+c/2+0.5,"M",a,b+c-1.5,"L",a+e,b+c-1.5]};B.prototype.callbacks.push(function(a){var b,e=a.options.exporting,
c=e.buttons;A=0;if(e.enabled!==!1){for(b in c)a.addButton(c[b]);u(a,"destroy",a.destroyExport)}})});