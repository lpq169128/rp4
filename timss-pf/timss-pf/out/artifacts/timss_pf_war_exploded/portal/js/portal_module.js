var portalConfig = [];

/*Snippet : calendar.snippet.config*/
portalConfig = window.portalConfig || [];
(function(_P){

	portalConfig.push({
		row: 6,
		col: 3,
		id : "calendar",
		module : "calendar",
		name : "请假日历",
		privilege:"homepage_portal_view_calendar",
		title : "请假日历"
	});
	
	portalConfig.push({
		row: 6,
		col: 0,
		id : "stat",
		module : "stat",
		privilege:"homepage_portal_view_stat",
		dataUrl : "../attendance/stat/queryPersonStat.do?date=" + new Date().getTime(),
		title : "本年个人考勤统计",
		name : "本年个人考勤统计"
		
	});
	
	
})(_portal);
/*Snippet : homepage.snippet.config*/
portalConfig = window.portalConfig || [];
(function(_P){
	/*----------------首页站内信息--------------------*/
	portalConfig.push({
		row : 0,
		col : 0,
		sizeX:6,
		sizeY:6,
		module : "hopList",
		id : "noticelist",
		name : "站内信息",
		title : "站内信息",
		columns : [
		    {title:"时间",width:"20",field:"statusdate", style:{"margin-right":"7px"}},
		    {title:"内容",width:"130",field:"content"},
		    {title:"状态",width:"0",field:"statusname", style:{"display":"none"}},
		    {title:"",width:"0",field:"contents", style:{"display":"none"}},
		    {title:"跳转路径",width:"0",field:"url", style:{"display":"none"}},
		    {title:"名称",width:"0",field:"name", style:{"display":"none"}},
		    {title:"编号",width:"0",field:"id", style:{"display":"none"}}
		],
		dataUrl : "../homepage/noticeInfo/noticeList.do?configdate=" + new Date().getTime,
		resizable : true,
		handles : ["e","s"]
	});
	/*----------------站点用户使用情况--------------------*/
	portalConfig.push({
		row: 0,
		col: 12,
		sizeX:6,
		sizeY:6,
		id : "siteuserloginfohopcharts",
		module : "hopcharts",
		privilege:"homepage_portal_view_hop_charts",
		name:"站点活跃度",
		title : "站点活跃度",
		dataUrl:"../homepage/noticeInfo/siteUserActiveInfo.do?configdate=" + new Date().getTime,
		options : {
			 options: {
				colors: ['#7cb5ec', '#434348', '#90ed7d', '#f7a35c', '#8085e9', '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'],
				xAxis: {
					categories: ['7天前', '6天前', '5天前', '4天前', '3天前','2天前','1天前']
				},
				yAxis: {
					title: {
						text: ''
					},
					plotLines: [{
						value: 0,
						width: 1,
						color: '#808080'
					}],
					tickPositions: [0, 20, 40, 60, 80, 100]
				},
				tooltip: {
					formatter : function(){
                		var s = '<b>' + this.x + '</b>';
						$.each(this.points, function () {
							var site = homepageDataIns.realData[this.series.name];
							var logCount = 0;
							if(typeof site != 'undefined'){
								typeof site[this.key] != 'undefined'? logCount = site[this.key] : 0;
							}
                    		s += '<br/>' + this.series.name + ':登录 ' + logCount + ' 人/次';
                		});
						return s;
					},
					shared: true
				},
				legend: {
					layout: 'vertical',
					align: 'right',
					verticalAlign: 'middle',
					borderWidth: 0
				}
			  },
 			loading: false,
 			useHighStocks: false,
 			title: {
	            text: ''
	        }
		}
	});
})(_portal);
/*Snippet : pur_inv.snippet.config*/
portalConfig = window.portalConfig || [];
(function(_P){

	portalConfig.push({
		row: 12,
		col: 0,
		sizeX:3,
		sizeY:2,
		name: "年度采购金额",
		id : "purtotalprice",
		module : "totalprice",
		privilege:"homepage_portal_view_purchase",
		dataUrl : "../purchase/purreport/purPriceTotal.do?date=" + new Date().getTime(),
		options : {
			iconBackground : "bsafe-skyblue",
			title : "年度采购金额",
			unit : "万元",
			icon : "icon-exchange"
		}
	});
	
	/*TIM-2274 原为"库存金额"卡片，后改为"暂估库存金额",取值等于"当前库存金额统计"卡片的已报账未报账的总和 */
	/*TIM-3344 "暂估库存金额"卡片更名为“实时库存金额”*/
	portalConfig.push({
		row: 14,
		col: 0,
		sizeX:3,
		sizeY:2,
		name:"实时库存金额",
		id : "invtotalprice",
		module : "totalprice",
		privilege:"homepage_portal_view_inventory",
		dataUrl : "../purchase/purreport/invPriceTotal.do?date=" + new Date().getTime(),
		options : {
			iconBackground : "bsafe-lightgreen",
			title : "实时库存金额",
			unit : "万元",
			icon : "icon-home"
		}
	});
	
	/*TIM-2274 增加"实时库存金额"卡片,等于"当前库存金额统计"卡片中的"已报账"的值 */
	/*TIM-3344 “实时库存金额”改名为“库存金额(不暂估)” */
	portalConfig.push({
		row: 14,
		col: 0,
		sizeX:3,
		sizeY:2,
		name:"库存金额(不暂估)",
		id : "actualTotal",
		module : "actualTotal",
		privilege:"homepage_portal_view_actual_total",
		dataUrl : "../purchase/purreport/invActualTotal.do?date=" + new Date().getTime(),
		options : {
			iconBackground : "bsafe-lightgreen",
			title : "库存金额(不暂估)",
			unit : "万元",
			icon : "icon-home"
		}
	});
	
	portalConfig.push({
		row : 0,
		col : 6,
		module : "stockList",
		id : "safetystock",
		sizeX:6,
		sizeY:6,
		title : "库存不足提醒",
		name : "库存不足提醒",
		columns : [
		    {title:"物资",width:"60",field:"itemname"},
		    {title:"型号",width:"80",field:"cusmodel"},
		    {title:"当前库存量",width:"35",field:"stockqty"},
		    {title:"安全库存量",width:"35",field:"lowinv"},
		    {title:"单位",width:"35",field:"unit"}
		],
		privilege:"homepage_portal_view_purchase",
		dataUrl : "../purchase/purreport/invSafetyStock.do?date=" + new Date().getTime(),
		resizable : false
	});
	
	/*----------------本年度各专业采购金额占比--------------------*/
	portalConfig.push({
		row: 0,
		col: 12,
		id : "majorPurchaseStatistic",
		module : "majorPurchaseStatistic",
		name:"本年度各专业采购金额占比",
		title : "本年度各专业采购金额占比",
		privilege:"homepage_portal_view_majorpurchase",
		dataUrl:"../purchase/purreport/majorPurchaseStatistic.do?date=" + new Date().getTime(),
		options : {
			 options: {
			      chart: {
			          type: 'pie'
			      },
			      tooltip: {
			            formatter: function () {
			                return ' <b>' + this.key + ' : ' + (this.y/10000).toFixed(2) + '万元</b>';
			            }
			       },
			       plotOptions: {
			            pie: {
			                allowPointSelect: true,
			                cursor: 'pointer',
			                dataLabels: {
			                    enabled: false
			                },
			                showInLegend:true
			            }
			        },
			       legend: {
			       		align: 'right', //水平方向位置
						verticalAlign: 'top', //垂直方向位置
						width:200,
						itemWidth:150,
						x: 0, //距离x轴的距离
						y: 0, //距离Y轴的距离
						labelFormatter: function () {
					          return "<span style='display:block;width:100px;float:left;'>"+this.name+"</span><span>"+ (this.y/this.total*100).toFixed(2)+'%'+"<span>";
					    },
					    itemStyle: {
					        fontWeight: 'normal'
					    },
					    useHTML: true
			       }
			},
 			loading: false,
 			useHighStocks: false,
 			title: {
	            text: ''
	        }
		}
	});
	
	/*----------------当前库存金额统计，分已报账和未报账--------------------*/
	portalConfig.push({
		row: 0,
		col: 12,
		id : "reimbursedMoneyStatistic",
		module : "reimbursedMoneyStatistic",
		name:"当前库存金额统计",
		title : "当前库存金额统计",
		privilege:"homepage_portal_view_reimbursed_money",
		dataUrl:"../purchase/purreport/reimbursedMoneyStatistic.do?date=" + new Date().getTime(),
		options : {
			 options: {
			      chart: {
			          type: 'pie'
			      },
		          colors : [_P.colors.lightgreen,_P.colors.red],
			      tooltip: {
			            formatter: function () {
			                return ' <b>' + this.key + ' : ' + this.y + '万元</b>';
			            }
			       },
			       plotOptions: {
			            pie: {
			                allowPointSelect: true,
			                cursor: 'pointer',
			                dataLabels: {
			                    enabled: false
			                },
			                showInLegend:true
			            }
			       },
			       legend: {
			       		align: 'right', //水平方向位置
						verticalAlign: 'top', //垂直方向位置
						width:200,
						itemWidth:150,
						x: 0, //距离x轴的距离
						y: 0, //距离Y轴的距离
						labelFormatter: function () {
							var label = "<span style='display:block;width:100px;float:left;'>" + this.name + "："+ this.y +"万元<span>";							
							if(this.name == "未报账"){
								var yData = this.series.yData;
								var total = yData[0] + yData[1];
								label += "<p style='margin:0 0 0 -22px;line-height:24px'>总金额(含暂估)：" + total.toFixed(4) + "万元</p>";
							} 
					        return label; 
					    },
					    itemStyle: {
					        fontWeight: 'normal'
					    },
					    useHTML: true
			       }
			},
 			loading: false,
 			useHighStocks: false,
 			title: {
	            text: ''
	        }
		}
	});
})(_portal);