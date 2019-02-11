
/*Snippet : calendar.snippet.js*/
!function(c){function f(){return new Date(Date.UTC.apply(Date,arguments))}function a(){var g=new Date();return f(g.getUTCFullYear(),g.getUTCMonth(),g.getUTCDate(),g.getUTCHours(),g.getUTCMinutes(),g.getUTCSeconds(),0)}var e=function(i,g){var j=this;this.element=c(i);this.language=g.language||this.element.data("date-language")||"en";this.language=this.language in d?this.language:"en";this.isRTL=d[this.language].rtl||false;this.formatType=g.formatType||this.element.data("format-type")||"standard";this.format=b.parseFormat(g.format||this.element.data("date-format")||d[this.language].format||b.getDefaultFormat(this.formatType,"input"),this.formatType);this.isInline=false;this.isVisible=false;this.isInput=this.element.is("input");this.bootcssVer=this.isInput?(this.element.is(".form-control")?3:2):(this.bootcssVer=this.element.is(".input-group")?3:2);this.component=this.element.is(".date")?(this.bootcssVer==3?this.element.find(".input-group-addon .glyphicon-th, .input-group-addon .glyphicon-time, .input-group-addon .glyphicon-calendar").parent():this.element.find(".add-on .icon-th, .add-on .icon-time, .add-on .icon-calendar").parent()):false;this.componentReset=this.element.is(".date")?(this.bootcssVer==3?this.element.find(".input-group-addon .glyphicon-remove").parent():this.element.find(".add-on .icon-remove").parent()):false;this.hasInput=this.component&&this.element.find("input").length;if(this.component&&this.component.length===0){this.component=false}this.linkField=g.linkField||this.element.data("link-field")||false;this.linkFormat=b.parseFormat(g.linkFormat||this.element.data("link-format")||b.getDefaultFormat(this.formatType,"link"),this.formatType);this.minuteStep=g.minuteStep||this.element.data("minute-step")||5;this.pickerPosition=g.pickerPosition||this.element.data("picker-position")||"bottom-right";this.showMeridian=g.showMeridian||this.element.data("show-meridian")||false;this.initialDate=g.initialDate||new Date();this.beforeShowDay=g.beforeShowDay||function(){};this._attachEvents();this.formatViewType="datetime";if("formatViewType" in g){this.formatViewType=g.formatViewType}else{if("formatViewType" in this.element.data()){this.formatViewType=this.element.data("formatViewType")}}this.minView=0;if("minView" in g){this.minView=g.minView}else{if("minView" in this.element.data()){this.minView=this.element.data("min-view")}}this.minView=b.convertViewMode(this.minView);this.maxView=b.modes.length-1;if("maxView" in g){this.maxView=g.maxView}else{if("maxView" in this.element.data()){this.maxView=this.element.data("max-view")}}this.maxView=b.convertViewMode(this.maxView);this.wheelViewModeNavigation=false;if("wheelViewModeNavigation" in g){this.wheelViewModeNavigation=g.wheelViewModeNavigation}else{if("wheelViewModeNavigation" in this.element.data()){this.wheelViewModeNavigation=this.element.data("view-mode-wheel-navigation")}}this.wheelViewModeNavigationInverseDirection=false;if("wheelViewModeNavigationInverseDirection" in g){this.wheelViewModeNavigationInverseDirection=g.wheelViewModeNavigationInverseDirection}else{if("wheelViewModeNavigationInverseDirection" in this.element.data()){this.wheelViewModeNavigationInverseDirection=this.element.data("view-mode-wheel-navigation-inverse-dir")}}this.wheelViewModeNavigationDelay=100;if("wheelViewModeNavigationDelay" in g){this.wheelViewModeNavigationDelay=g.wheelViewModeNavigationDelay}else{if("wheelViewModeNavigationDelay" in this.element.data()){this.wheelViewModeNavigationDelay=this.element.data("view-mode-wheel-navigation-delay")}}this.startViewMode=2;if("startView" in g){this.startViewMode=g.startView}else{if("startView" in this.element.data()){this.startViewMode=this.element.data("start-view")}}this.startViewMode=b.convertViewMode(this.startViewMode);this.viewMode=this.startViewMode;this.viewSelect=this.minView;if("viewSelect" in g){this.viewSelect=g.viewSelect}else{if("viewSelect" in this.element.data()){this.viewSelect=this.element.data("view-select")}}this.viewSelect=b.convertViewMode(this.viewSelect);this.forceParse=true;if("forceParse" in g){this.forceParse=g.forceParse}else{if("dateForceParse" in this.element.data()){this.forceParse=this.element.data("date-force-parse")}}var h=window._dialogEmmbed?_parent().$("body"):(this.isInline?this.element:"body");this.picker=c((this.bootcssVer==3)?b.templateV3:b.template).appendTo(h).on({click:c.proxy(this.click,this),mousedown:c.proxy(this.mousedown,this)});if(this.wheelViewModeNavigation){if(c.fn.mousewheel){this.picker.on({mousewheel:c.proxy(this.mousewheel,this)})}else{console.log("Mouse Wheel event is not supported. Please include the jQuery Mouse Wheel plugin before enabling this option")}}if(this.isInline){this.picker.addClass("datetimepicker-inline")}else{this.picker.addClass("datetimepicker-dropdown-"+this.pickerPosition+" dropdown-menu")}if(this.isRTL){this.picker.addClass("datetimepicker-rtl");if(this.bootcssVer==3){this.picker.find(".prev span, .next span").toggleClass("glyphicon-arrow-left glyphicon-arrow-right")}else{this.picker.find(".prev i, .next i").toggleClass("icon-arrow-left icon-arrow-right")}}c(document).on("mousedown",function(k){if(c(k.target).closest(".datetimepicker").length===0){j.hide()}});if(window._dialogEmmbed){_parent().$(".window-mask").on("mousedown",function(k){j.hide()})}this.autoclose=false;if("autoclose" in g){this.autoclose=g.autoclose}else{if("dateAutoclose" in this.element.data()){this.autoclose=this.element.data("date-autoclose")}}this.keyboardNavigation=true;if("keyboardNavigation" in g){this.keyboardNavigation=g.keyboardNavigation}else{if("dateKeyboardNavigation" in this.element.data()){this.keyboardNavigation=this.element.data("date-keyboard-navigation")}}this.todayBtn=(g.todayBtn||this.element.data("date-today-btn")||false);this.todayHighlight=(g.todayHighlight||this.element.data("date-today-highlight")||false);this.weekStart=((g.weekStart||this.element.data("date-weekstart")||d[this.language].weekStart||0)%7);this.weekEnd=((this.weekStart+6)%7);this.startDate=-Infinity;this.endDate=Infinity;this.daysOfWeekDisabled=[];this.setStartDate(g.startDate||this.element.data("date-startdate"));this.setEndDate(g.endDate||this.element.data("date-enddate"));this.setDaysOfWeekDisabled(g.daysOfWeekDisabled||this.element.data("date-days-of-week-disabled"));this.fillDow();this.fillMonths();this.update();this.showMode();if(this.isInline){this.show()}};e.prototype={constructor:e,_events:[],_attachEvents:function(){this._detachEvents();if(this.isInput){this._events=[[this.element,{focus:c.proxy(this.show,this),keyup:c.proxy(this.update,this),keydown:c.proxy(this.keydown,this)}]]}else{if(this.component&&this.hasInput){this._events=[[this.element.find("input"),{focus:c.proxy(this.show,this),keyup:c.proxy(this.update,this),keydown:c.proxy(this.keydown,this)}],[this.component,{click:c.proxy(this.show,this)}]];if(this.componentReset){this._events.push([this.componentReset,{click:c.proxy(this.reset,this)}])}}else{if(this.element.is("div")){this.isInline=true}else{this._events=[[this.element,{click:c.proxy(this.show,this)}]]}}}for(var g=0,h,j;g<this._events.length;g++){h=this._events[g][0];j=this._events[g][1];h.on(j)}},_detachEvents:function(){for(var g=0,h,j;g<this._events.length;g++){h=this._events[g][0];j=this._events[g][1];h.off(j)}this._events=[]},show:function(g){this.picker.show();this.height=this.component?this.component.outerHeight():this.element.outerHeight();if(this.forceParse){this.update()}this.place();c(window).on("resize",c.proxy(this.place,this));if(g){g.stopPropagation();g.preventDefault()}this.isVisible=true;this.element.trigger({type:"show",date:this.date})},hide:function(h){if(!this.isVisible){return}if(this.isInline){return}this.picker.hide();c(window).off("resize",this.place);this.viewMode=this.startViewMode;this.showMode();if(!this.isInput){c(document).off("mousedown",this.hide)}if(this.forceParse&&(this.isInput&&this.element.val()||this.hasInput&&this.element.find("input").val())){this.setValue()}this.isVisible=false;this.element.trigger({type:"hide",date:this.date});var g=this.isInput&&this.element||this.hasInput&&this.element.find("input");if(g.hasClass("valid-field")){g.valid()}},remove:function(){this._detachEvents();this.picker.remove();delete this.picker;delete this.element.data().datetimepicker},getDate:function(){var g=this.getUTCDate();return new Date(g.getTime()+(g.getTimezoneOffset()*60000))},getUTCDate:function(){return this.date},setDate:function(g){this.setUTCDate(new Date(g.getTime()-(g.getTimezoneOffset()*60000)))},setUTCDate:function(g){if(g>=this.startDate&&g<=this.endDate){this.date=g;this.setValue();this.viewDate=this.date;this.fill()}else{this.element.trigger({type:"outOfRange",date:g,startDate:this.startDate,endDate:this.endDate})}},setFormat:function(h){this.format=b.parseFormat(h,this.formatType);var g;if(this.isInput){g=this.element}else{if(this.component){g=this.element.find("input")}}if(g&&g.val()){this.setValue()}},setValue:function(){var g=this.getFormattedDate();if(!this.isInput){if(this.component){this.element.find("input").val(g)}this.element.data("date",g)}else{this.element.val(g)}if(this.linkField){c("#"+this.linkField).val(this.getFormattedDate(this.linkFormat))}},getFormattedDate:function(g){if(g==undefined){g=this.format}return b.formatDate(this.date,g,this.language,this.formatType)},setStartDate:function(g){this.startDate=g||-Infinity;if(this.startDate!==-Infinity){this.startDate=b.parseDate(this.startDate,this.format,this.language,this.formatType)}this.update();this.updateNavArrows()},setEndDate:function(g){this.endDate=g||Infinity;if(this.endDate!==Infinity){this.endDate=b.parseDate(this.endDate,this.format,this.language,this.formatType)}this.update();this.updateNavArrows()},setDaysOfWeekDisabled:function(g){this.daysOfWeekDisabled=g||[];if(!c.isArray(this.daysOfWeekDisabled)){this.daysOfWeekDisabled=this.daysOfWeekDisabled.split(/,\s*/)}this.daysOfWeekDisabled=c.map(this.daysOfWeekDisabled,function(h){return parseInt(h,10)});this.update();this.updateNavArrows()},place:function(){if(this.isInline){return}var i=0;c("div").each(function(){var n=parseInt(c(this).css("zIndex"),10);if(n>i){i=n}});var m=window._dialogEmmbed?(_itc_getmaskindex()+10):(i+10);var l,k,j;if(this.component){l=this.component.offset();j=l.left;if(this.pickerPosition=="bottom-left"||this.pickerPosition=="top-left"){j+=this.component.outerWidth()-this.picker.outerWidth()}}else{l=this.element.offset();j=l.left}if(this.pickerPosition=="top-left"||this.pickerPosition=="top-right"){k=l.top-this.picker.outerHeight()}else{k=l.top+this.height}if(window._dialogEmmbed){var h=c("body");var g=_itc_getdlgoffset();k+=g.top+26-h.scrollTop();j+=g.left-h.scrollLeft()}this.picker.css({top:k,left:j,zIndex:m})},update:function(){var g,h=false;if(arguments&&arguments.length&&(typeof arguments[0]==="string"||arguments[0] instanceof Date)){g=arguments[0];h=true}else{g=this.element.data("date")||(this.isInput?this.element.val():this.element.find("input").val())||this.initialDate;if(typeof g=="string"||g instanceof String){g=g.replace(/^\s+|\s+$/g,"")}}if(!g){g=new Date();h=false}this.date=b.parseDate(g,this.format,this.language,this.formatType);if(h){this.setValue()}if(this.date<this.startDate){this.viewDate=new Date(this.startDate)}else{if(this.date>this.endDate){this.viewDate=new Date(this.endDate)}else{this.viewDate=new Date(this.date)}}this.fill()},fillDow:function(){var g=this.weekStart,h="<tr>";while(g<this.weekStart+7){h+='<th class="dow">'+d[this.language].daysMin[(g++)%7]+"</th>"}h+="</tr>";this.picker.find(".datetimepicker-days thead").append(h)},fillMonths:function(){var h="",g=0;while(g<12){h+='<span class="month">'+d[this.language].monthsShort[g++]+"月</span>"}this.picker.find(".datetimepicker-months td").html(h)},fill:function(){if(this.date==null||this.viewDate==null){return}var F=new Date(this.viewDate),q=F.getUTCFullYear(),G=F.getUTCMonth(),j=F.getUTCDate(),A=F.getUTCHours(),v=F.getUTCMinutes(),w=this.startDate!==-Infinity?this.startDate.getUTCFullYear():-Infinity,B=this.startDate!==-Infinity?this.startDate.getUTCMonth():-Infinity,l=this.endDate!==Infinity?this.endDate.getUTCFullYear():Infinity,x=this.endDate!==Infinity?this.endDate.getUTCMonth():Infinity,n=(new f(this.date.getUTCFullYear(),this.date.getUTCMonth(),this.date.getUTCDate())).valueOf(),E=new Date();this.picker.find(".datetimepicker-days thead th:eq(1)").text(q+"年"+(G+1)+"月");if(this.formatViewType=="time"){var C=A%12?A%12:12;var h=(C<10?"0":"")+C;var m=(v<10?"0":"")+v;var I=d[this.language].meridiem[A<12?0:1];this.picker.find(".datetimepicker-hours thead th:eq(1)").text(h+":"+m+" "+I.toUpperCase());this.picker.find(".datetimepicker-minutes thead th:eq(1)").text(h+":"+m+" "+I.toUpperCase())}else{this.picker.find(".datetimepicker-hours thead th:eq(1)").text(q+"年"+(G+1)+"月"+j+"日");this.picker.find(".datetimepicker-minutes thead th:eq(1)").text(q+"年"+(G+1)+"月"+j+"日")}this.picker.find("tfoot th.today").text(d[this.language].today).toggle(this.todayBtn!==false);this.updateNavArrows();this.fillMonths();var J=f(q,G-1,28,0,0,0,0),z=b.getDaysInMonth(J.getUTCFullYear(),J.getUTCMonth());J.setUTCDate(z);J.setUTCDate(z-(J.getUTCDay()-this.weekStart+7)%7);var g=new Date(J);g.setUTCDate(g.getUTCDate()+42);g=g.valueOf();var o=[];var s;while(J.valueOf()<g){if(J.getUTCDay()==this.weekStart){o.push("<tr>")}s="";if(J.getUTCFullYear()<q||(J.getUTCFullYear()==q&&J.getUTCMonth()<G)){s+=" old"}else{if(J.getUTCFullYear()>q||(J.getUTCFullYear()==q&&J.getUTCMonth()>G)){s+=" new"}}if(this.todayHighlight&&J.getUTCFullYear()==E.getFullYear()&&J.getUTCMonth()==E.getMonth()&&J.getUTCDate()==E.getDate()){s+=" today"}if(J.valueOf()==n){s+=" active"}if((J.valueOf()+86400000)<=this.startDate||J.valueOf()>this.endDate||c.inArray(J.getUTCDay(),this.daysOfWeekDisabled)!==-1){s+=" disabled"}if(this.beforeShowDay!==c.noop){var r=this.beforeShowDay(J);if(r===undefined){r={}}else{if(typeof(r)==="boolean"){r={enabled:r}}else{if(typeof(r)==="string"){r={classes:r}}}}if(r.enabled===false){s+="disabled"}if(r.classes){s+=" "+r.classes}if(r.tooltip){tooltip=r.tooltip}}o.push('<td class="day'+s+'">'+J.getUTCDate()+"</td>");if(J.getUTCDay()==this.weekEnd){o.push("</tr>")}J.setUTCDate(J.getUTCDate()+1)}this.picker.find(".datetimepicker-days tbody").empty().append(o.join(""));o=[];var t="",D="",p="";for(var y=0;y<24;y++){var u=f(q,G,j,y);s="";if((u.valueOf()+3600000)<=this.startDate||u.valueOf()>this.endDate){s+=" disabled"}else{if(A==y){s+=" active"}}if(this.showMeridian&&d[this.language].meridiem.length==2){D=(y<12?d[this.language].meridiem[0]:d[this.language].meridiem[1]);if(D!=p){if(p!=""){o.push("</fieldset>")}o.push('<fieldset class="hour"><legend>'+D.toUpperCase()+"</legend>")}p=D;t=(y%12?y%12:12);o.push('<span class="hour'+s+" hour_"+(y<12?"am":"pm")+'">'+t+"</span>");if(y==23){o.push("</fieldset>")}}else{t=y+":00";o.push('<span class="hour'+s+'">'+t+"</span>")}}this.picker.find(".datetimepicker-hours td").html(o.join(""));o=[];t="",D="",p="";for(var y=0;y<60;y+=this.minuteStep){var u=f(q,G,j,A,y,0);s="";if(u.valueOf()<this.startDate||u.valueOf()>this.endDate){s+=" disabled"}else{if(Math.floor(v/this.minuteStep)==Math.floor(y/this.minuteStep)){s+=" active"}}if(this.showMeridian&&d[this.language].meridiem.length==2){D=(A<12?d[this.language].meridiem[0]:d[this.language].meridiem[1]);if(D!=p){if(p!=""){o.push("</fieldset>")}o.push('<fieldset class="minute"><legend>'+D.toUpperCase()+"</legend>")}p=D;t=(A%12?A%12:12);o.push('<span class="minute'+s+'">'+t+":"+(y<10?"0"+y:y)+"</span>");if(y==59){o.push("</fieldset>")}}else{t=y+":00";o.push('<span class="minute'+s+'">'+A+":"+(y<10?"0"+y:y)+"</span>")}}this.picker.find(".datetimepicker-minutes td").html(o.join(""));var K=this.date.getUTCFullYear();var k=this.picker.find(".datetimepicker-months").find("th:eq(1)").text(q).end().find("span").removeClass("active");if(K==q){k.eq(this.date.getUTCMonth()).addClass("active")}if(q<w||q>l){k.addClass("disabled")}if(q==w){k.slice(0,B).addClass("disabled")}if(q==l){k.slice(x+1).addClass("disabled")}o="";q=parseInt(q/10,10)*10;var H=this.picker.find(".datetimepicker-years").find("th:eq(1)").text(q+"-"+(q+9)).end().find("td");q-=1;for(var y=-1;y<11;y++){o+='<span class="year'+(y==-1||y==10?" old":"")+(K==q?" active":"")+(q<w||q>l?" disabled":"")+'">'+q+"</span>";q+=1}H.html(o);this.place();this.element.trigger({type:"dateFill",date:F})},updateNavArrows:function(){var k=new Date(this.viewDate),i=k.getUTCFullYear(),j=k.getUTCMonth(),h=k.getUTCDate(),g=k.getUTCHours();switch(this.viewMode){case 0:if(this.startDate!==-Infinity&&i<=this.startDate.getUTCFullYear()&&j<=this.startDate.getUTCMonth()&&h<=this.startDate.getUTCDate()&&g<=this.startDate.getUTCHours()){this.picker.find(".prev").css({visibility:"hidden"})}else{this.picker.find(".prev").css({visibility:"visible"})}if(this.endDate!==Infinity&&i>=this.endDate.getUTCFullYear()&&j>=this.endDate.getUTCMonth()&&h>=this.endDate.getUTCDate()&&g>=this.endDate.getUTCHours()){this.picker.find(".next").css({visibility:"hidden"})}else{this.picker.find(".next").css({visibility:"visible"})}break;case 1:if(this.startDate!==-Infinity&&i<=this.startDate.getUTCFullYear()&&j<=this.startDate.getUTCMonth()&&h<=this.startDate.getUTCDate()){this.picker.find(".prev").css({visibility:"hidden"})}else{this.picker.find(".prev").css({visibility:"visible"})}if(this.endDate!==Infinity&&i>=this.endDate.getUTCFullYear()&&j>=this.endDate.getUTCMonth()&&h>=this.endDate.getUTCDate()){this.picker.find(".next").css({visibility:"hidden"})}else{this.picker.find(".next").css({visibility:"visible"})}break;case 2:if(this.startDate!==-Infinity&&i<=this.startDate.getUTCFullYear()&&j<=this.startDate.getUTCMonth()){this.picker.find(".prev").css({visibility:"hidden"})}else{this.picker.find(".prev").css({visibility:"visible"})}if(this.endDate!==Infinity&&i>=this.endDate.getUTCFullYear()&&j>=this.endDate.getUTCMonth()){this.picker.find(".next").css({visibility:"hidden"})}else{this.picker.find(".next").css({visibility:"visible"})}break;case 3:case 4:if(this.startDate!==-Infinity&&i<=this.startDate.getUTCFullYear()){this.picker.find(".prev").css({visibility:"hidden"})}else{this.picker.find(".prev").css({visibility:"visible"})}if(this.endDate!==Infinity&&i>=this.endDate.getUTCFullYear()){this.picker.find(".next").css({visibility:"hidden"})}else{this.picker.find(".next").css({visibility:"visible"})}break}},mousewheel:function(h){h.preventDefault();h.stopPropagation();if(this.wheelPause){return}this.wheelPause=true;var g=h.originalEvent;var j=g.wheelDelta;var i=j>0?1:(j===0)?0:-1;if(this.wheelViewModeNavigationInverseDirection){i=-i}this.showMode(i);setTimeout(c.proxy(function(){this.wheelPause=false},this),this.wheelViewModeNavigationDelay)},click:function(k){k.stopPropagation();k.preventDefault();var l=c(k.target).closest("span, td, th, legend");if(l.length==1){if(l.is(".disabled")){this.element.trigger({type:"outOfRange",date:this.viewDate,startDate:this.startDate,endDate:this.endDate});return}switch(l[0].nodeName.toLowerCase()){case"th":switch(l[0].className){case"switch":this.showMode(1);break;case"prev":case"next":var g=b.modes[this.viewMode].navStep*(l[0].className=="prev"?-1:1);switch(this.viewMode){case 0:this.viewDate=this.moveHour(this.viewDate,g);break;case 1:this.viewDate=this.moveDate(this.viewDate,g);break;case 2:this.viewDate=this.moveMonth(this.viewDate,g);break;case 3:case 4:this.viewDate=this.moveYear(this.viewDate,g);break}this.fill();break;case"today":var h=new Date();h=f(h.getFullYear(),h.getMonth(),h.getDate(),h.getHours(),h.getMinutes(),h.getSeconds(),0);if(h<this.startDate){h=this.startDate}else{if(h>this.endDate){h=this.endDate}}this.viewMode=this.startViewMode;this.showMode(0);this._setDate(h);this.fill();if(this.autoclose){this.hide()}break}break;case"span":if(!l.is(".disabled")){var n=this.viewDate.getUTCFullYear(),m=this.viewDate.getUTCMonth(),o=this.viewDate.getUTCDate(),p=this.viewDate.getUTCHours(),i=this.viewDate.getUTCMinutes(),q=this.viewDate.getUTCSeconds();if(l.is(".month")){this.viewDate.setUTCDate(1);m=l.parent().find("span").index(l);o=this.viewDate.getUTCDate();this.viewDate.setUTCMonth(m);this.element.trigger({type:"changeMonth",date:this.viewDate});if(this.viewSelect>=3){this._setDate(f(n,m,o,p,i,q,0))}}else{if(l.is(".year")){this.viewDate.setUTCDate(1);n=parseInt(l.text(),10)||0;this.viewDate.setUTCFullYear(n);this.element.trigger({type:"changeYear",date:this.viewDate});if(this.viewSelect>=4){this._setDate(f(n,m,o,p,i,q,0))}}else{if(l.is(".hour")){p=parseInt(l.text(),10)||0;if(l.hasClass("hour_am")||l.hasClass("hour_pm")){if(p==12&&l.hasClass("hour_am")){p=0}else{if(p!=12&&l.hasClass("hour_pm")){p+=12}}}this.viewDate.setUTCHours(p);this.element.trigger({type:"changeHour",date:this.viewDate});if(this.viewSelect>=1){this._setDate(f(n,m,o,p,i,q,0))}}else{if(l.is(".minute")){i=parseInt(l.text().substr(l.text().indexOf(":")+1),10)||0;this.viewDate.setUTCMinutes(i);this.element.trigger({type:"changeMinute",date:this.viewDate});if(this.viewSelect>=0){this._setDate(f(n,m,o,p,i,q,0))}}}}}if(this.viewMode!=0){var j=this.viewMode;this.showMode(-1);this.fill();if(j==this.viewMode&&this.autoclose){this.hide()}}else{this.fill();if(this.autoclose){this.hide()}}}break;case"td":if(l.is(".day")&&!l.is(".disabled")){var o=parseInt(l.text(),10)||1;var n=this.viewDate.getUTCFullYear(),m=this.viewDate.getUTCMonth(),p=this.viewDate.getUTCHours(),i=this.viewDate.getUTCMinutes(),q=this.viewDate.getUTCSeconds();if(l.is(".old")){if(m===0){m=11;n-=1}else{m-=1}}else{if(l.is(".new")){if(m==11){m=0;n+=1}else{m+=1}}}this.viewDate.setUTCFullYear(n);this.viewDate.setUTCMonth(m,o);this.element.trigger({type:"changeDay",date:this.viewDate});if(this.viewSelect>=2){this._setDate(f(n,m,o,p,i,q,0))}}var j=this.viewMode;this.showMode(-1);this.fill();if(j==this.viewMode&&this.autoclose){this.hide()}break}}},_setDate:function(g,i){if(!i||i=="date"){this.date=g}if(!i||i=="view"){this.viewDate=g}this.fill();this.setValue();var h;if(this.isInput){h=this.element}else{if(this.component){h=this.element.find("input")}}if(h){h.change();if(this.autoclose&&(!i||i=="date")){}}this.element.trigger({type:"changeDate",date:this.date})},moveMinute:function(h,g){if(!g){return h}var i=new Date(h.valueOf());i.setUTCMinutes(i.getUTCMinutes()+(g*this.minuteStep));return i},moveHour:function(h,g){if(!g){return h}var i=new Date(h.valueOf());i.setUTCHours(i.getUTCHours()+g);return i},moveDate:function(h,g){if(!g){return h}var i=new Date(h.valueOf());i.setUTCDate(i.getUTCDate()+g);return i},moveMonth:function(g,h){if(!h){return g}var l=new Date(g.valueOf()),p=l.getUTCDate(),m=l.getUTCMonth(),k=Math.abs(h),o,n;h=h>0?1:-1;if(k==1){n=h==-1?function(){return l.getUTCMonth()==m}:function(){return l.getUTCMonth()!=o};o=m+h;l.setUTCMonth(o);if(o<0||o>11){o=(o+12)%12}}else{for(var j=0;j<k;j++){l=this.moveMonth(l,h)}o=l.getUTCMonth();l.setUTCDate(p);n=function(){return o!=l.getUTCMonth()}}while(n()){l.setUTCDate(--p);l.setUTCMonth(o)}return l},moveYear:function(h,g){return this.moveMonth(h,g*12)},dateWithinRange:function(g){return g>=this.startDate&&g<=this.endDate},keydown:function(k){if(this.picker.is(":not(:visible)")){if(k.keyCode==27){this.show()}return}var m=false,h,n,l,o,g;switch(k.keyCode){case 27:this.hide();k.preventDefault();break;case 37:case 39:if(!this.keyboardNavigation){break}h=k.keyCode==37?-1:1;viewMode=this.viewMode;if(k.ctrlKey){viewMode+=2}else{if(k.shiftKey){viewMode+=1}}if(viewMode==4){o=this.moveYear(this.date,h);g=this.moveYear(this.viewDate,h)}else{if(viewMode==3){o=this.moveMonth(this.date,h);g=this.moveMonth(this.viewDate,h)}else{if(viewMode==2){o=this.moveDate(this.date,h);g=this.moveDate(this.viewDate,h)}else{if(viewMode==1){o=this.moveHour(this.date,h);g=this.moveHour(this.viewDate,h)}else{if(viewMode==0){o=this.moveMinute(this.date,h);g=this.moveMinute(this.viewDate,h)}}}}}if(this.dateWithinRange(o)){this.date=o;this.viewDate=g;this.setValue();this.update();k.preventDefault();m=true}break;case 38:case 40:if(!this.keyboardNavigation){break}h=k.keyCode==38?-1:1;viewMode=this.viewMode;if(k.ctrlKey){viewMode+=2}else{if(k.shiftKey){viewMode+=1}}if(viewMode==4){o=this.moveYear(this.date,h);g=this.moveYear(this.viewDate,h)}else{if(viewMode==3){o=this.moveMonth(this.date,h);g=this.moveMonth(this.viewDate,h)}else{if(viewMode==2){o=this.moveDate(this.date,h*7);g=this.moveDate(this.viewDate,h*7)}else{if(viewMode==1){if(this.showMeridian){o=this.moveHour(this.date,h*6);g=this.moveHour(this.viewDate,h*6)}else{o=this.moveHour(this.date,h*4);g=this.moveHour(this.viewDate,h*4)}}else{if(viewMode==0){o=this.moveMinute(this.date,h*4);g=this.moveMinute(this.viewDate,h*4)}}}}}if(this.dateWithinRange(o)){this.date=o;this.viewDate=g;this.setValue();this.update();k.preventDefault();m=true}break;case 13:if(this.viewMode!=0){var j=this.viewMode;this.showMode(-1);this.fill();if(j==this.viewMode&&this.autoclose){this.hide()}}else{this.fill();if(this.autoclose){this.hide()}}k.preventDefault();break;case 9:this.hide();break}if(m){var i;if(this.isInput){i=this.element}else{if(this.component){i=this.element.find("input")}}if(i){i.change()}this.element.trigger({type:"changeDate",date:this.date})}},showMode:function(g){if(g){var h=Math.max(0,Math.min(b.modes.length-1,this.viewMode+g));if(h>=this.minView&&h<=this.maxView){this.element.trigger({type:"changeMode",date:this.viewDate,oldViewMode:this.viewMode,newViewMode:h});this.viewMode=h}}this.picker.find(">div").hide().filter(".datetimepicker-"+b.modes[this.viewMode].clsName).css("display","block");this.updateNavArrows()},reset:function(g){this._setDate(null,"date")}};c.fn.datetimepicker=function(h){var g=Array.apply(null,arguments);g.shift();return this.each(function(){var k=c(this),j=k.data("datetimepicker"),i=typeof h=="object"&&h;if(!j){k.data("datetimepicker",(j=new e(this,c.extend({},c.fn.datetimepicker.defaults,i))))}if(typeof h=="string"&&typeof j[h]=="function"){j[h].apply(j,g)}})};c.fn.datetimepicker.defaults={};c.fn.datetimepicker.Constructor=e;var d=c.fn.datetimepicker.dates={en:{days:["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"],daysShort:["Sun","Mon","Tue","Wed","Thu","Fri","Sat","Sun"],daysMin:["Su","Mo","Tu","We","Th","Fr","Sa","Su"],months:["January","February","March","April","May","June","July","August","September","October","November","December"],monthsShort:["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"],meridiem:["am","pm"],suffix:["st","nd","rd","th"],today:"Today"},ch:{days:["星期日","星期一","星期二","星期三","星期四","星期五","星期六","星期日"],daysShort:["日","一","二","三","四","五","六","日"],daysMin:["日","一","二","三","四","五","六","日"],months:["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],monthsShort:["一","二","三","四","五","六","七","八","九","十","十一","十二"],meridiem:["上午","下午"],suffix:["st","nd","rd","th"],today:"今天"}};var b={modes:[{clsName:"minutes",navFnc:"Hours",navStep:1},{clsName:"hours",navFnc:"Date",navStep:1},{clsName:"days",navFnc:"Month",navStep:1},{clsName:"months",navFnc:"FullYear",navStep:1},{clsName:"years",navFnc:"FullYear",navStep:10}],isLeapYear:function(g){return(((g%4===0)&&(g%100!==0))||(g%400===0))},getDaysInMonth:function(g,h){return[31,(b.isLeapYear(g)?29:28),31,30,31,30,31,31,30,31,30,31][h]},getDefaultFormat:function(g,h){if(g=="standard"){if(h=="input"){return"yyyy-mm-dd hh:ii"}else{return"yyyy-mm-dd hh:ii:ss"}}else{if(g=="php"){if(h=="input"){return"Y-m-d H:i"}else{return"Y-m-d H:i:s"}}else{throw new Error("Invalid format type.")}}},validParts:function(g){if(g=="standard"){return/hh?|HH?|p|P|ii?|ss?|dd?|DD?|mm?|MM?|yy(?:yy)?/g}else{if(g=="php"){return/[dDjlNwzFmMnStyYaABgGhHis]/g}else{throw new Error("Invalid format type.")}}},nonpunctuation:/[^ -\/:-@\[-`{-~\t\n\rTZ]+/g,parseFormat:function(j,h){var g=j.replace(this.validParts(h),"\0").split("\0"),i=j.match(this.validParts(h));if(!g||!g.length||!i||i.length==0){throw new Error("Invalid date format.")}return{separators:g,parts:i}},parseDate:function(l,u,o,r){if(l instanceof Date){var w=new Date(l.valueOf()-l.getTimezoneOffset()*60000);w.setMilliseconds(0);return w}if(/^\d{4}\-\d{1,2}\-\d{1,2}$/.test(l)){u=this.parseFormat("yyyy-mm-dd",r)}if(/^\d{4}\-\d{1,2}\-\d{1,2}[T ]\d{1,2}\:\d{1,2}$/.test(l)){u=this.parseFormat("yyyy-mm-dd hh:ii",r)}if(/^\d{4}\-\d{1,2}\-\d{1,2}[T ]\d{1,2}\:\d{1,2}\:\d{1,2}[Z]{0,1}$/.test(l)){u=this.parseFormat("yyyy-mm-dd hh:ii:ss",r)}if(/^[-+]\d+[dmwy]([\s,]+[-+]\d+[dmwy])*$/.test(l)){var x=/([-+]\d+)([dmwy])/,m=l.match(/([-+]\d+)([dmwy])/g),g,k;l=new Date();for(var n=0;n<m.length;n++){g=x.exec(m[n]);k=parseInt(g[1]);switch(g[2]){case"d":l.setUTCDate(l.getUTCDate()+k);break;case"m":l=e.prototype.moveMonth.call(e.prototype,l,k);break;case"w":l.setUTCDate(l.getUTCDate()+k*7);break;case"y":l=e.prototype.moveYear.call(e.prototype,l,k);break}}return f(l.getUTCFullYear(),l.getUTCMonth(),l.getUTCDate(),l.getUTCHours(),l.getUTCMinutes(),l.getUTCSeconds(),0)}var m=l&&l.match(this.nonpunctuation)||[],l=new Date(0,0,0,0,0,0,0),q={},t=["hh","h","ii","i","ss","s","yyyy","yy","M","MM","m","mm","D","DD","d","dd","H","HH","p","P"],v={hh:function(s,i){return s.setUTCHours(i)},h:function(s,i){return s.setUTCHours(i)},HH:function(s,i){return s.setUTCHours(i==12?0:i)},H:function(s,i){return s.setUTCHours(i==12?0:i)},ii:function(s,i){return s.setUTCMinutes(i)},i:function(s,i){return s.setUTCMinutes(i)},ss:function(s,i){return s.setUTCSeconds(i)},s:function(s,i){return s.setUTCSeconds(i)},yyyy:function(s,i){return s.setUTCFullYear(i)},yy:function(s,i){return s.setUTCFullYear(2000+i)},m:function(s,i){i-=1;while(i<0){i+=12}i%=12;s.setUTCMonth(i);while(s.getUTCMonth()!=i){s.setUTCDate(s.getUTCDate()-1)}return s},d:function(s,i){return s.setUTCDate(i)},p:function(s,i){return s.setUTCHours(i==1?s.getUTCHours()+12:s.getUTCHours())}},j,p,g;v.M=v.MM=v.mm=v.m;v.dd=v.d;v.P=v.p;l=f(l.getFullYear(),l.getMonth(),l.getDate(),l.getHours(),l.getMinutes(),l.getSeconds());if(m.length==u.parts.length){for(var n=0,h=u.parts.length;n<h;n++){j=parseInt(m[n],10);g=u.parts[n];if(isNaN(j)){switch(g){case"MM":p=c(d[o].months).filter(function(){var i=this.slice(0,m[n].length),s=m[n].slice(0,i.length);return i==s});j=c.inArray(p[0],d[o].months)+1;break;case"M":p=c(d[o].monthsShort).filter(function(){var i=this.slice(0,m[n].length),s=m[n].slice(0,i.length);return i==s});j=c.inArray(p[0],d[o].monthsShort)+1;break;case"p":case"P":j=c.inArray(m[n].toLowerCase(),d[o].meridiem);break}}q[g]=j}for(var n=0,y;n<t.length;n++){y=t[n];if(y in q&&!isNaN(q[y])){v[y](l,q[y])}}}return l},formatDate:function(g,m,o,k){if(g==null){return""}var n;if(k=="standard"){n={yy:g.getUTCFullYear().toString().substring(2),yyyy:g.getUTCFullYear(),m:g.getUTCMonth()+1,M:d[o].monthsShort[g.getUTCMonth()],MM:d[o].months[g.getUTCMonth()],d:g.getUTCDate(),D:d[o].daysShort[g.getUTCDay()],DD:d[o].days[g.getUTCDay()],p:(d[o].meridiem.length==2?d[o].meridiem[g.getUTCHours()<12?0:1]:""),h:g.getUTCHours(),i:g.getUTCMinutes(),s:g.getUTCSeconds()};if(d[o].meridiem.length==2){n.H=(n.h%12==0?12:n.h%12)}else{n.H=n.h}n.HH=(n.H<10?"0":"")+n.H;n.P=n.p.toUpperCase();n.hh=(n.h<10?"0":"")+n.h;n.ii=(n.i<10?"0":"")+n.i;n.ss=(n.s<10?"0":"")+n.s;n.dd=(n.d<10?"0":"")+n.d;n.mm=(n.m<10?"0":"")+n.m}else{if(k=="php"){n={y:g.getUTCFullYear().toString().substring(2),Y:g.getUTCFullYear(),F:d[o].months[g.getUTCMonth()],M:d[o].monthsShort[g.getUTCMonth()],n:g.getUTCMonth()+1,t:b.getDaysInMonth(g.getUTCFullYear(),g.getUTCMonth()),j:g.getUTCDate(),l:d[o].days[g.getUTCDay()],D:d[o].daysShort[g.getUTCDay()],w:g.getUTCDay(),N:(g.getUTCDay()==0?7:g.getUTCDay()),S:(g.getUTCDate()%10<=d[o].suffix.length?d[o].suffix[g.getUTCDate()%10-1]:""),a:(d[o].meridiem.length==2?d[o].meridiem[g.getUTCHours()<12?0:1]:""),g:(g.getUTCHours()%12==0?12:g.getUTCHours()%12),G:g.getUTCHours(),i:g.getUTCMinutes(),s:g.getUTCSeconds()};n.m=(n.n<10?"0":"")+n.n;n.d=(n.j<10?"0":"")+n.j;n.A=n.a.toString().toUpperCase();n.h=(n.g<10?"0":"")+n.g;n.H=(n.G<10?"0":"")+n.G;n.i=(n.i<10?"0":"")+n.i;n.s=(n.s<10?"0":"")+n.s}else{throw new Error("Invalid format type.")}}var g=[],l=c.extend([],m.separators);for(var j=0,h=m.parts.length;j<h;j++){if(l.length){g.push(l.shift())}g.push(n[m.parts[j]])}if(l.length){g.push(l.shift())}return g.join("")},convertViewMode:function(g){switch(g){case 4:case"decade":g=4;break;case 3:case"year":g=3;break;case 2:case"month":g=2;break;case 1:case"day":g=1;break;case 0:case"hour":g=0;break}return g},headTemplate:'<thead><tr class="lx_th1"><th class="prev">&lt;&lt;</th><th colspan="5" class="switch"></th><th class="next">&gt;&gt;</th></tr></thead>',headTemplateV3:'<thead><tr class="lx_th1"><th class="prev">&lt;&lt; </th><th colspan="5" class="switch"></th><th class="next">&gt;&gt;</th></tr></thead>',contTemplate:'<tbody><tr><td colspan="7"></td></tr></tbody>',footTemplate:'<tfoot><tr><th colspan="7" class="today"></th></tr></tfoot>'};b.template='<div class="datetimepicker"><div class="datetimepicker-minutes"><table class=" table-condensed">'+b.headTemplate+b.contTemplate+b.footTemplate+'</table></div><div class="datetimepicker-hours"><table class=" table-condensed">'+b.headTemplate+b.contTemplate+b.footTemplate+'</table></div><div class="datetimepicker-days"><table class=" table-condensed">'+b.headTemplate+"<tbody></tbody>"+b.footTemplate+'</table></div><div class="datetimepicker-months"><table class="table-condensed">'+b.headTemplate+b.contTemplate+b.footTemplate+'</table></div><div class="datetimepicker-years"><table class="table-condensed">'+b.headTemplate+b.contTemplate+b.footTemplate+"</table></div></div>";b.templateV3='<div class="datetimepicker"><div class="datetimepicker-minutes"><table class=" table-condensed">'+b.headTemplateV3+b.contTemplate+b.footTemplate+'</table></div><div class="datetimepicker-hours"><table class=" table-condensed">'+b.headTemplateV3+b.contTemplate+b.footTemplate+'</table></div><div class="datetimepicker-days"><table class=" table-condensed">'+b.headTemplateV3+"<tbody></tbody>"+b.footTemplate+'</table></div><div class="datetimepicker-months"><table class="table-condensed">'+b.headTemplateV3+b.contTemplate+b.footTemplate+'</table></div><div class="datetimepicker-years"><table class="table-condensed">'+b.headTemplateV3+b.contTemplate+b.footTemplate+"</table></div></div>";c.fn.datetimepicker.DPGlobal=b;c.fn.datetimepicker.noConflict=function(){c.fn.datetimepicker=old;return this};c(document).on("focus.datetimepicker.data-api click.datetimepicker.data-api",'[data-provide="datetimepicker"]',function(h){var g=c(this);if(g.data("datetimepicker")){return}h.preventDefault();g.datetimepicker("show")});c(function(){c('[data-provide="datetimepicker-inline"]').datetimepicker()})}(window.jQuery);

/**** 日历扩展 ****/
+function(c){var b=function(e,d){this.type="";this.options="";this.enabled="";this.timeout="";this.hoverState="";this.$element=null;this.init("tooltip",e,d)};b.DEFAULTS={animation:true,placement:"top",selector:false,template:'<div class="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>',trigger:"hover focus",title:"",delay:0,html:false,container:false};b.prototype.init=function(k,h,f){this.enabled=true;this.type=k;this.$element=c(h);this.options=this.getOptions(f);var j=this.options.trigger.split(" ");for(var g=j.length;g--;){var e=j[g];if(e=="click"){this.$element.on("click."+this.type,this.options.selector,c.proxy(this.toggle,this))}else{if(e!="manual"){var l=e=="hover"?"mouseenter":"focus";var d=e=="hover"?"mouseleave":"blur";this.$element.on(l+"."+this.type,this.options.selector,c.proxy(this.enter,this));this.$element.on(d+"."+this.type,this.options.selector,c.proxy(this.leave,this))}}}this.options.selector?(this._options=c.extend({},this.options,{trigger:"manual",selector:""})):this.fixTitle()};b.prototype.getDefaults=function(){return b.DEFAULTS};b.prototype.getOptions=function(d){d=c.extend({},this.getDefaults(),this.$element.data(),d);if(d.delay&&typeof d.delay=="number"){d.delay={show:d.delay,hide:d.delay}}return d};b.prototype.getDelegateOptions=function(){var d={};var e=this.getDefaults();this._options&&c.each(this._options,function(f,g){if(e[f]!=g){d[f]=g}});return d};b.prototype.enter=function(e){var d=e instanceof this.constructor?e:c(e.currentTarget)[this.type](this.getDelegateOptions()).data("bs."+this.type);clearTimeout(d.timeout);d.hoverState="in";if(!d.options.delay||!d.options.delay.show){return d.show()}d.timeout=setTimeout(function(){if(d.hoverState=="in"){d.show()}},d.options.delay.show)};b.prototype.leave=function(e){var d=e instanceof this.constructor?e:c(e.currentTarget)[this.type](this.getDelegateOptions()).data("bs."+this.type);clearTimeout(d.timeout);d.hoverState="out";if(!d.options.delay||!d.options.delay.hide){return d.hide()}d.timeout=setTimeout(function(){if(d.hoverState=="out"){d.hide()}},d.options.delay.hide)};b.prototype.show=function(){var n=c.Event("show.bs."+this.type);if(this.hasContent()&&this.enabled){this.$element.trigger(n);if(n.isDefaultPrevented()){return}var j=this.tip();this.setContent();if(this.options.animation){j.addClass("fade")}var i=typeof this.options.placement=="function"?this.options.placement.call(this,j[0],this.$element[0]):this.options.placement;var r=/\s?auto?\s?/i;var s=r.test(i);if(s){i=i.replace(r,"")||"top"}j.detach().css({top:0,left:0,display:"block"}).addClass(i);this.options.container?j.appendTo(this.options.container):j.insertAfter(this.$element);var o=this.getPosition();var d=j[0].offsetWidth;var l=j[0].offsetHeight;if(s){var h=this.$element.parent();var g=i;var p=document.documentElement.scrollTop||document.body.scrollTop;var q=this.options.container=="body"?window.innerWidth:h.outerWidth();var m=this.options.container=="body"?window.innerHeight:h.outerHeight();var k=this.options.container=="body"?0:h.offset().left;i=i=="bottom"&&o.top+o.height+l-p>m?"top":i=="top"&&o.top-p-l<0?"bottom":i=="right"&&o.right+d>q?"left":i=="left"&&o.left-d<k?"right":i;j.removeClass(g).addClass(i)}var f=this.getCalculatedOffset(i,o,d,l);this.applyPlacement(f,i);this.$element.trigger("shown.bs."+this.type)}};b.prototype.applyPlacement=function(i,j){var g=false;var k=this.tip();var f=k[0].offsetWidth;var n=k[0].offsetHeight;var e=parseInt(k.css("margin-top"),10);var h=parseInt(k.css("margin-left"),10);if(isNaN(e)){e=0}if(isNaN(h)){h=0}i.top=i.top+e;i.left=i.left+h;k.offset(i).addClass("in");var d=k[0].offsetWidth;var l=k[0].offsetHeight;if(j=="top"&&l!=n){g=true;i.top=i.top+n-l}if(/bottom|top/.test(j)){var m=0;if(i.left<0){m=i.left*-2;i.left=0;k.offset(i);d=k[0].offsetWidth;l=k[0].offsetHeight}this.replaceArrow(m-f+d,d,"left")}else{this.replaceArrow(l-n,l,"top")}if(g){k.offset(i)}};b.prototype.replaceArrow=function(f,e,d){this.arrow().css(d,f?(50*(1-f/e)+"%"):"")};b.prototype.setContent=function(){var e=this.tip();var d=this.getTitle();e.find(".tooltip-inner")[this.options.html?"html":"text"](d);e.removeClass("fade in top bottom left right")};b.prototype.hide=function(){var f=this;var h=this.tip();var g=c.Event("hide.bs."+this.type);function d(){if(f.hoverState!="in"){h.detach()}}this.$element.trigger(g);if(g.isDefaultPrevented()){return}h.removeClass("in");c.support.transition&&this.$tip.hasClass("fade")?h.one(c.support.transition.end,d).emulateTransitionEnd(150):d();this.$element.trigger("hidden.bs."+this.type);return this};b.prototype.fixTitle=function(){var d=this.$element;if(d.attr("title")||typeof(d.attr("data-original-title"))!="string"){d.attr("data-original-title",d.attr("title")||"").attr("title","")}};b.prototype.hasContent=function(){return this.getTitle()};b.prototype.getPosition=function(){var d=this.$element[0];return c.extend({},(typeof d.getBoundingClientRect=="function")?d.getBoundingClientRect():{width:d.offsetWidth,height:d.offsetHeight},this.$element.offset())};b.prototype.getCalculatedOffset=function(d,g,e,f){return d=="bottom"?{top:g.top+g.height,left:g.left+g.width/2-e/2}:d=="top"?{top:g.top-f,left:g.left+g.width/2-e/2}:d=="left"?{top:g.top+g.height/2-f/2,left:g.left-e}:{top:g.top+g.height/2-f/2,left:g.left+g.width}};b.prototype.getTitle=function(){var f;var d=this.$element;var e=this.options;f=d.attr("data-original-title")||(typeof e.title=="function"?e.title.call(d[0]):e.title);return f};b.prototype.tip=function(){return this.$tip=this.$tip||c(this.options.template)};b.prototype.arrow=function(){return this.$arrow=this.$arrow||this.tip().find(".tooltip-arrow")};b.prototype.validate=function(){if(!this.$element[0].parentNode){this.hide();this.$element=null;this.options=null}};b.prototype.enable=function(){this.enabled=true};b.prototype.disable=function(){this.enabled=false};b.prototype.toggleEnabled=function(){this.enabled=!this.enabled};b.prototype.toggle=function(f){var d=f?c(f.currentTarget)[this.type](this.getDelegateOptions()).data("bs."+this.type):this;d.tip().hasClass("in")?d.leave(d):d.enter(d)};b.prototype.destroy=function(){this.hide().$element.off("."+this.type).removeData("bs."+this.type)};var a=c.fn.tooltip;c.fn.tooltip=function(d){return this.each(function(){var g=c(this);var f=g.data("bs.tooltip");var e=typeof d=="object"&&d;if(!f){g.data("bs.tooltip",(f=new b(this,e)))}if(typeof d=="string"){f[d]()}})};c.fn.tooltip.Constructor=b;c.fn.tooltip.noConflict=function(){c.fn.tooltip=a;return this}}(jQuery);+function(c){var b=function(e,d){this.init("popover",e,d)};if(!c.fn.tooltip){throw new Error("Popover requires tooltip.js")}b.DEFAULTS=c.extend({},c.fn.tooltip.Constructor.DEFAULTS,{placement:"right",trigger:"click",content:"",template:'<div class="popover"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'});b.prototype=c.extend({},c.fn.tooltip.Constructor.prototype);b.prototype.constructor=b;b.prototype.getDefaults=function(){return b.DEFAULTS};b.prototype.setContent=function(){var f=this.tip();var e=this.getTitle();var d=this.getContent();f.find(".popover-title")[this.options.html?"html":"text"](e);f.find(".popover-content")[this.options.html?"html":"text"](d);f.removeClass("fade top bottom left right in");if(!f.find(".popover-title").html()){f.find(".popover-title").hide()}};b.prototype.hasContent=function(){return this.getTitle()||this.getContent()};b.prototype.getContent=function(){var d=this.$element;var e=this.options;return d.attr("data-content")||(typeof e.content=="function"?e.content.call(d[0]):e.content)};b.prototype.arrow=function(){return this.$arrow=this.$arrow||this.tip().find(".arrow")};b.prototype.tip=function(){if(!this.$tip){this.$tip=c(this.options.template)}return this.$tip};var a=c.fn.popover;c.fn.popover=function(d){return this.each(function(){var g=c(this);var f=g.data("bs.popover");var e=typeof d=="object"&&d;if(!f){g.data("bs.popover",(f=new b(this,e)))}if(typeof d=="string"){f[d]()}})};c.fn.popover.Constructor=b;c.fn.popover.noConflict=function(){c.fn.popover=a;return this}}(jQuery);


(function(_P){
/***************************calendar defMods begin***************************/
	_P.definedModules.calendar = {
		sizeX : 3,
		sizeY : 6,
		bordered : true,
		module : "calendar",
		template : "calendar",
		resizable : false,
		noPadding : "lr",
		titleStyle : {
			paddingBottom : 0
		}

	};
	
	_P.definedModules.stat = {
			sizeX : 3,
			sizeY : 6,
			bordered : true,
			module : "simple-porlet",
			template : "stat"
		};
	
	/***************************calendar defMods end***************************/
	
	/***************************calendar template begin***************************/
    _P.templates.calendar = '<div id="dtp3" class="right bbox" style="margin-left:4px;" ></div>';
    
    _P.templates.stat = '<span class="attand-item">年假剩余<span style="font-size:36px" class="csafe-darkgreen">{{" " + g.surplusAnnual.toFixed(1) + " "}}</span>天</span>'
    	+ '<span class="attand-item">请假累计<span style="font-size:36px" class="csafe-orange">{{" " + g.countDays.toFixed(1) + " " }}</span>天</span>';
    
    /***************************calendar template end***************************/
	
	/***************************calendar directive begin***************************/
	app.directive('calendar',function($compile){
		return {
			link : function(scope,element,attrs){
				var template = _P.templates.calendar;
				var el = $compile(template)(scope);
				

				function showPopup(){	//新的方法，有培训班和课程
					$("#dtp3").find(".underdot").each(function(){
						//仔细思考一下，还有更好的写法没？一次为这么多格子创建对象会有性能问题么？
						var classes = $(this).attr("class");
						var clsSplit = classes.split(" ");
						var month = -1;
						var year = -1;
						for(var i=0;i<clsSplit.length;i++){
							var cls = clsSplit[i];
							if(cls.substr(0,6)=="month_"){
								month = cls.substr(6);
							}
							if(cls.substr(0,5)=="year_"){
								year = cls.substr(5);
							}
						}
						var day = $(this).html();
						var _this = $(this);
						_this.attr("year",year);
						_this.attr("month",month);
						_this.attr("day",day);
						_this.mouseover(function(){
							var __this = $(this);
							var y = __this.attr("year");
							var m = __this.attr("month");
							var d = __this.attr("day");
							var cStr = y + "-" + m + "-" + d;
							$.post(basePath + "attendance/leave/queryCalendarByDay.do",{"ctime":cStr},function(data){
								if(data.result == "fail"){
									return;
								}
								var result = data.statVos;
								var popTitle = y + "年" + m + "月" + d + "日";
								$("body").children(".popover").remove();
								var popContent = "";
								for(var i=0;i<result.length;i++){
									var rs = result[i];
									popContent += "<span class='pop_class_title' style='float:left;height:auto;'>";
									/*popContent += "<span style='' class='blue_tri'></span>";*/
									popContent += "<span style='float:left;'>" + rs.userName +"</span>"; 
									popContent += "<span style='float:left;color: #666666;font-size:14px;margin-left:12px;'>" + rs.userId + "</span>";
									popContent += "</span>";
									popContent += "<div style='clear:both;width:100%;margin-top:4px'></div>";
									popContent += "<div style='clear:both;width:100%;margin-top:4px'></div>";
								}	
								
								var offset = __this.offset();
								var scnWidth = $("body").width();
								var objWidth = $(__this).width();
								var leftSize = offset.left;
								var rightSize = scnWidth - leftSize - objWidth;
								var direction = leftSize > rightSize ? "left" : "right";
								$("body").children(".popover").remove();
								if(__this.popover){
									__this.popover('destroy');
								}
								__this.popover({
									"html":true,
									"title":popTitle,
									"content":popContent,
									"trigger":"hover",
									"placement":direction,
									"container":"body"});
								__this.popover("show");
							});
						});	
						
						_this.mouseout(function(){
							var __this = $(this);
							$("body").children(".popover").remove();
							if(__this.popover){
								__this.popover('destroy');
							}
						});
					});
				}

				
				function initDtp(){
					$('#dtp3').datetimepicker({
				        language:  'ch',
				        weekStart: 1,
						autoclose: 1,
						todayHighlight: 1,
						startView: 2,
						minView: 2,
						maxView:2,
						forceParse: 0,
						todayBtn:false,
						beforeShowDay : function(date){
							var day = date.getDate();
							var month = date.getMonth();
							var year = date.getFullYear();
							var smonth = month<10? "0" + month:month;
							var courseDays = window.courseDays || [];
							for(var i=0;i<courseDays.length;i++){
								var d = courseDays[i];
								if(date.getTime()>=d[0] && date.getTime()<=d[1]){
									return "underdot month_" + (month + 1) + " year_" + year;
								}
							}						
						}
					}).on("dateFill",function(d){
						showPopup();
						$.post(basePath + "attendance/leave/queryCalendarByMonth.do",{
							year : d.date.getFullYear(),
							month : d.date.getMonth()+1
						},function(data){
							if(data.result == "success"){
								var courseDays = [];
								var result = data.statVos;
								for(var i=0;i<result.length;i++){
									courseDays.push([result[i].startDate,result[i].endDate]);
								}
								window.courseDays = courseDays;
							}
						});	
					});
					showPopup();
				}
				
				
				
				setTimeout(function(){
					//必须在渲染之前加载一次课程的初始数据
					var now = new Date();
					$.post(basePath + "attendance/leave/queryCalendarByMonth.do",{
						year : now.getFullYear(),
						month : now.getMonth()+1
					},function(data){
						if(data.result == 'success'){
							var result =  data.statVos;
							var courseDays = [];
							for(var i=0;i<result.length;i++){
								courseDays.push([result[i].startDate,result[i].endDate]);
							}
							window.courseDays = courseDays;
						}
						initDtp();
					});	
				},100);
				element.replaceWith(el);
			}
		};
	});
	

	/***************************calendar directive end***************************/
})(_portal);
/*Snippet : homepage.snippet.js*/
function homepageData() {
	this.conPos = [];
	this.hasPos = false;
	this.realData = null;/*站点活跃度真实登陆数据*/
	this.setHighcharts = function(){
		setHighchartsText();
	};
	
	var timeoutCount = 0;
	var totalTimeoutCount = 5;
	
	var setHighchartsText = function(){
		var jq = $('#siteuserloginfohopcharts').find("text:contains('Highcharts')");
		jq.remove();
		if(timeoutCount++ < totalTimeoutCount){
			setTimeout(setHighchartsText, 5);
		}
	};
	this.getReSizeDate = function(dataRows, boxSize) {
		if (!this.hasPos) {
			for ( var i = 0; i < dataRows.length; i++) {
				var oneRow = dataRows[i];
				var cPos = null;
				var contents = null;
				for ( var j = 0; j < oneRow.length; j++) {
					var oneObject = oneRow[j];
					if ("content" == oneObject.id) {
						cPos = {
							i : i,
							j : j
						};
					} else if ("contents" == oneObject.id) {
						contents = oneObject.value;
					}
					if (null != cPos && null != contents) {
						break;
					}
				}
				this.conPos.push({
					pos : cPos,
					contents : contents
				});
			}
			this.hasPos = true;
		}
		var subflag = null;
		if (201 > boxSize.width) {
			subflag = {
				beg : 0,
				end : 15
			};
		} else if (201 < boxSize.width && boxSize.width < 440) {
			subflag = {
				beg : 0,
				end : 26
			};
		} else if (440 < boxSize.width && boxSize.width < 680) {
			subflag = {
				beg : 0,
				end : 50
			};
		} else if (680 < boxSize.width && boxSize.width < 910) {
			subflag = {
				beg : 0,
				end : 90
			};
		} else if (910 < boxSize.width && boxSize.width < 1146) {
			subflag = {
				beg : 0,
				end : 165
			};
		} else {
			subflag = {
				beg : 0,
				end : 170
			};
		}
		for ( var i = 0; i < this.conPos.length; i++) {
			var con = this.conPos[i];
			if (con.contents.length > subflag.end) {
				dataRows[con.pos.i][con.pos.j].value = con.contents.substr(
						subflag.beg, subflag.end)
						+ "...";
			} else {
				dataRows[con.pos.i][con.pos.j].value = con.contents;
			}
		}
	};
}

var homepageDataIns = new homepageData();

(function(_P) {
	/** *************************hop defMods begin************************** */

	_P.definedModules.hopList = {
		sizeX : 3,
		bordered : true,
		module : "hoplist",
		flag : true,
		sum : true,
		noPadding : "lr"
	};

	_P.definedModules.hopcharts = {
		sizeX : 6,
		sizeY : 6,
		noPadding : "lr",
		bordered : true,
		module : "highcharts",
		resizable : false
	};

	/** *************************hop defMods end************************** */

	/** *************************hop template begin************************** */
	_P.templates.list = '<div style="width:95%" class="homepage">'
			+ '<ul>'
			+ '<li ng-repeat="row in rowData" ng-style="calcStyle($index)" ng-dblclick="calcClick(row, $index)">'
			+ '<span id="{{field.id}}" ng-repeat="field in row" ng-style="{{field.style}}" width="{{field.w}}">{{field.value}}</span>'
			+ '</li>'
			+ '</ul>'
			+ '</div>'
			+ '<div style="width:95%;display:none;" class="homepage" id="hop-notice-none">'
			+ '<div style="margin-left: 41%;margin-top: 13%;font-size:12px;">无最新信息<div></div>';
	/** *************************hop template end************************** */

	/** *************************hop directive begin************************** */

	app.directive('highcharts', function($compile) {
		return {
			link : function(scope, element, attrs) {
				scope.$on('portal-data-loaded', function() {
					var id = scope.item.id;
					if ('siteuserloginfohopcharts' == id) {
						var data = scope.g;
						homepageDataIns.realData = data.xrData;
						for(var index = 0; index < data.xlable.length;index++ ){
							var chartConfig = scope.chartConfig;
							chartConfig.options.xAxis.categories[index] = data.xlable[index];
						}
						homepageDataIns.setHighcharts();
					}
				});
			}
		};
	});

	app
			.directive(
					'hoplist',
					function($compile) {
						return {
							link : function(scope, element, attrs) {
								var rowData = new Array();
								var item = scope.item;
								scope.options = item.options;
								scope.columns = item.columns;

								scope.calcStyle = function(index) {
									var row = scope.g[index];
									var cssVal = "";
									if("Notice" == row.status){
										cssVal = "url(../img/homepage/portal_forward.png)";
									}else if("Complete" == row.status){
										cssVal = "url(../img/homepage/portal_complete.png)";
									}else if("Info" == row.status){
										cssVal = "url(../img/homepage/portal_info.png)";
									}else if("Warning" == row.status){
										cssVal = "url(../img/homepage/portal_warning.png)";
									}
									return {
										"list-style-image" :cssVal
									};
								};

								scope.calcClick = function(row, index) {
									var curRow = scope.g[index];
									var _parent = window.parent;
									var url = _parent.basePath + curRow.url;
									var id = curRow.id;
									var name = curRow.name;
									var currTabId = _parent.FW
											.getCurrentTabId();
									var opts = {
										id : id,
										name : name,
										url : url,
										tabOpt : {
											closeable : true,
											afterClose : "FW.deleteTab('$arg');FW.activeTabById('"
													+ currTabId
													+ "');FW.getFrame('homepage').homepageServiceImpl.refresh();"
										}
									};
									window.parent._ITC.addTabWithTree(opts);
								};

								scope
										.$on(
												'portal-data-loaded',
												function() {
													var data = scope.g;
													var columns = scope.columns;
													for ( var i = 0; i < data.length; i++) {
														var row = data[i];
														var r = new Array();
														for ( var j = 0; j < columns.length; j++) {
															var column = columns[j];
															var fieldValue = row[column.field];
															if ("contents" == column.field) {
																fieldValue = row["content"];
															}
															fieldValue = typeof (fieldValue) !== "undefined" ? fieldValue
																	: "";
															r
																	.push({
																		id : column.field,
																		value : fieldValue,
																		w : column.width,
																		style : column.style
																	});
														}
														rowData.push(r);
													}
													scope.rowData = rowData;
													var boxSize = new Object();
													boxSize.width = parseInt(scope.infoBoxStyle.width);
													boxSize.height = parseInt(scope.infoBoxStyle.height);
													homepageDataIns
															.getReSizeDate(
																	scope.rowData,
																	boxSize);

													if ("undefined" == typeof data
															|| 0 == data.length) {
														$("#hop-notice-none")
																.show();
													}
												});

								scope.$on('infobox-size-changed', function(e,
										boxSize) {
									homepageDataIns.getReSizeDate(
											scope.rowData, boxSize);
								});

								scope.rowData = [];
								var el = $compile(_P.templates.list)(scope);
								element.replaceWith(el);
							}
						};
					});
	/** *************************hop directive end************************** */
})(_portal);
/*Snippet : pur_inv.snippet.js*/
(function(_P){
/***************************pur defMods begin***************************/
	_P.definedModules.totalprice = {
		sizeX : 3,
		sizeY : 2,
		bordered : false,
		module : "simple-porlet",
		template : "totalprice"
	};

	_P.definedModules.actualTotal = {
		sizeX : 3,
		sizeY : 2,
		bordered : false,
		module : "simple-porlet",
		template : "totalprice"
	};
	
	_P.definedModules.stockList = {
		sizeX : 3,
		bordered : true,
		module : "list",
		flag : true,
		sum : true,
		noPadding:"lr"
	};
	
	_P.definedModules.majorPurchaseStatistic = {
		sizeX : 6,
		sizeY : 9,
		bordered : true,
		module : "highcharts",
		resizable : false			
	};	
	//当前站点库存报账金额统计（分已报账和未报账）
	_P.definedModules.reimbursedMoneyStatistic = {
		sizeX : 6,
		sizeY : 6,
		bordered : true,
		module : "highcharts",
		resizable : false			
	};	
	
	/***************************pur defMods end***************************/
	
	/***************************pur template begin***************************/
    _P.templates.totalprice = '<div class="inumber-icon-wrap fl {{item.options.iconBackground}}">' +
								  	'<span class="{{item.options.icon}} inumber-icon"></span>' +
								  '</div>' +
								  '<div class="inumber-number-wrap">' +
								  	'<div class="inumber-title">{{item.options.title}}</div>'+
								  	'<div class="inumber-number">{{g.totalPrice}}<span>{{item.options.unit}}</span></div>'+
								  '</div>';
    
    /*_P.templates.invlist = '<div style="width:446px;height:173px;border-top:1px solid #aaa;overflow:auto">' +
    							'<table class="new-table" cellspacing=0 cellpadding=0 style="width:100%">' +
							    '<thead>' +
							         '<tr class="row-item head">' +
							             '<td class="col-left">物资</td>' + 
							             '<td class="col-right">当前库存量</td>' + 
							             '<td class="col-right">安全库存量</td>' + 
							         '</tr>'+
							    '</thead>' + 
							    '<tbody>' +
							    	  '<tr ng-repeat="row in rowData" class="row-item">' +
							    	       '<td class="col-left">' + 
							    	       		'<span class="row-top">{{row.itemname}}</span>' +
							    	       		'<span class="row-bottom">{{row.cusmodel}}</span>' +
							    	       '</td>' + 
							    	       '<td class="col-right align-right">' + 
							    	       		'<b>{{row.stockqty}}</b>' +
							    	       		'<span>{{row.unit}}</span>' +
						    	       	   '</td>' +
							    	       '<td class="col-right align-right">' + 
							    	       		'<span class="row-b">{{row.lowinv}}</span>' +
							    	       		'<span>{{row.unit}}</span>' +
						    	       	   '</td>' +
							    	  '</tr>' +
							    '</tbody>' +
							'</table>'+
    					'</div>';*/

    
    _P.templates.invlist = '<div style="width:446px;height:173px;border-top:1px solid #aaa;overflow:auto">' +
								'<ul class="new-table">' +
									'<li class="row-item head">' +
										'<div class="col-left">物资</div>' +
										'<div class="col-right">当前库存量</div>' +
										'<div class="col-right">安全库存量</div>' +
									'</li>' +
									'<li class="row-item" ng-repeat="row in rowData" style="list-style: none;">' +
									'	<div class="col-left">' +
									'		<span class="row-top" style="line-height: 32px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;display: block;float: left;width: 150px;">{{row.itemname}}</span>' +
                                    '		<span class="row-bottom" style="line-height: 32px;display: block;width: 70px;padding-top: 2px;padding-left: 2px;">{{row.warehousename}}</span>' +
                                    '		<span class="row-bottom">{{row.cusmodel}}</span>' +
									'	</div>' +
									'	<div class="col-right align-right">' +
									'		<b>{{row.stockqty}}</b>' +
									'		<span>{{row.unit}}</span>' +
									'	</div>' +
									'	<div class="col-right align-right">' +
									'		<span class="row-b">{{row.lowinv}}</span>' +
									'		<span>{{row.unit}}</span>' +
									'	</div>' +
									'</li>' +
								'</ul>' +
							'</div>';
    /***************************pur template end***************************/
	
	/***************************pur directive begin***************************/
	app.directive('totalprice',function($compile){
		return {
			link : function(scope,element,attrs){
				var template = _P.templates.totalprice;
				var el = $compile(template)(scope);
				element.replaceWith(el);
			}
		};
	});
	
	app.directive('list',function($compile){
		return {
			link: function(scope,element,attrs){
				var rowData = [];
				var item = scope.item;
				var preModule = _P.definedModules[item.module];
				scope.options = item.options;
				scope.columns = item.columns;
				scope.rowData = [];
				scope.$on('portal-data-loaded',function(){
					var data = scope.g;
					var columns = scope.columns;
					for(var i=0;i<data.length;i++){
						var row = data[i];
						/*var r = [];
						for(var j=0;j<columns.length;j++){
							var column = columns[j];
							var fieldValue = row[column.field];
							var fieldName = column.field;
							fieldValue = typeof(fieldValue) !== "undefined" ? fieldValue : "";
							r.push({c: fieldValue,w:column.width,style:column.style,f: fieldName});
						}*/
						rowData.push(row);
					}
					scope.rowData = rowData;
				});				
				var el = $compile(_P.templates.invlist)(scope);
				element.replaceWith(el);
			}
		};
	});
	
	/***************************pur directive end***************************/
})(_portal);