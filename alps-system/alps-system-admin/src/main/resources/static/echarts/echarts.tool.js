var Echart=function(){
	this.config={
		//颜色
		color:[],
		//容器
		divId:"",
		//主标题
		title:"",
		//副标题
		subtitle:"",
		//图例
		legend:[],	
		//x轴名称 
		xName:"",
		//x轴坐标
		xAxis:[],	 
		//y轴名称
		yName:"",			
		//y轴坐标
		yAxis:[],			
		//图例名称
		seriesName:"",		
		//柱状、折线数据
		seriesData:[],
		//饼图数据
		seriesPieData:[],
		//多组数据统计
		series:[],	
		//图表类型
		type:'',
		//单位
		tooltipText:"",
		//附属类
		stack:"",
		//子名称
		stackName:"",
		//子数据
		stackData:[],
		//平行子数据
		parallel:"",
		//平行子数据名称
		parallelName:"",
		//平行子数据
		parallelData:[],
		//是否坐标轴刻度与标签对齐
		isAlignWithLabel:false,
		//标题位置:left,center,right
		title_x:null,
		//图例位置:left,center,right
		legend_x:null,
		legend_y:null
	}
}


Echart.prototype.getData=function(url,params){
	$.ajax({
    	url:url,
    	type:"POST",
    	data:params,
    	success:function(result){
    		console.log(result);
    		var config=echart.config;
    		for(var i=0;i<result.length;i++){
    			var data=result[i]; 			
    			config.title=data.title;
   			    config.subtitle=data.subtitle;
   			    config.title_x="center";
   			    config.legend_y="bottom";
   			    config.xName=data.xName;
   			    config.yName=data.yName;
   			    config.legend=data.legend;
   			    config.xAxis=data.xAxis;
   			    config.divId="chart";
   			    config.series=data.series;
   			    config.seriesName=data.seriesName;
   				config.tooltipText=data.tooltipText;
   				
   				var chartType=data.type;
	    		if(chartType=='bar'){
	    			$(".chart").hide();
	    			$("#chart").show();
	    			echart.bar(config);
	    		}else if(chartType=='line'){
	    			$(".chart").hide();
	    			$("#chart").show();
	    			echart.line(config);
	    		}else if(chartType=='pie'){
	    			$(".chart").hide();
	    			var num=data.seriesPieDataList.length;
	    			if(num==1){
	    				$('#chart0').css('width','100%');
	    				$('#chart0').css('height','500');	    				
	    			}else if(num==2){
	    				$('#chart0').css('width','50%');
	    				$('#chart0').css('height','500');
	    				$('#chart1').css('width','50%');
	    				$('#chart1').css('height','500');
	    			}else{
	    				$('.chart').css('width','50%');
	    				$('.chart').css('height','350');
	    			}
	    			
	   			    for(var j=0;j<num;j++){
		   			    config.seriesPieData=data.seriesPieDataList[j];	
		   			    config.seriesName=data.seriesNameList[j];	
		   			    config.subtitle=data.seriesNameList[j];
		   			    
		   			    $("#chart"+j).show();
		   			    $("#chart").hide();
		   			 	config.divId="chart"+j;
		    			echart.pie(config);
	   			    }
	    		}else if(chartType=='guage'){
	    			
	    		}
    		}
    	}
    });
}

//柱状图
Echart.prototype.bar=function(config){
	echarts.init(document.getElementById(config.divId)).setOption(getOption(config,'bar'));
}

//折线图
Echart.prototype.line=function(config){
	echarts.init(document.getElementById(config.divId)).setOption(getOption(config,'line'));
}

//饼图
Echart.prototype.pie=function(config){
	echarts.init(document.getElementById(config.divId)).setOption(getOptionPie(config));
}

//折叠柱状图
Echart.prototype.stack=function(config){
	echarts.init(document.getElementById(config.divId)).setOption(getOptionStack(config));
}
//仪表盘
Echart.prototype.gauge=function(config){
	echarts.init(document.getElementById(config.divId)).setOption(getOptionGuage(config));
}
//雷达图
Echart.prototype.rader=function(config){
	echarts.init(document.getElementById(config.divId)).setOption(getOptionRader(config));
}

//柱状图、折线图option
function getOption(config,type){	
	var pointerType='shadow';
	var alignWithLabel=config.isAlignWithLabel;
	if(type=='line'){
		pointerType='line';
		alignWithLabel=true;
	}
	
	var option = {
			color:config.color,
			title: {
				text: config.title,
				subtext:config.subtitle,
				x:config.title_x||'left'
			},			 
			toolbox: {
               // x:100,
               // y:0,
                show : true,
                feature : {
                    dataView : {show: true, readOnly: true},
                    magicType : {show: true, type: ['bar','line']},
                    restore : {show: true},
                   saveAsImage : {show: true},
//                    myTool1: {
//                        show: true,
//                        title: '柱状图切换',
//                        icon: 'image://'+_path+'/static/libs/echarts/img/bar.png',
//                        onclick: function (){
//                        	onSearch('bar');                      	
//                        }
//                    },
//                    myTool2: {
//                        show: true,
//                        title: '折线图切换',
//                        icon: 'image://'+_path+'/static/libs/echarts/img/line.png',
//                        onclick: function (){
//                        	onSearch('line');                      	
//                        }
//                    },
//                    myTool: {
//                        show: true,
//                        title: '饼图切换',
//                        icon: 'image://'+_path+'/static/libs/echarts/img/pie.png',
//                        onclick: function (){
//                        	onSearch('pie');                     	
//                        }
//                    }
                }
            },
			tooltip: {
				trigger:'axis',
				axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : pointerType     // 默认为直线，可选为：'line' | 'shadow'
		        }
				//,formatter: '{a}<br />{b}:{c}'+config.tooltipText
			},			
			legend: {
				data:config.legend,
				x:config.legend_x||'center',
				y:config.legend_y||'top'
			},
			xAxis: {
				name:config.xName,
				type: 'category',
				axisTick: {
					alignWithLabel: alignWithLabel
				},
				data: config.xAxis
			},
			yAxis: {
				name:config.yName,
				type: 'value'
			},
			series: [{
				name: config.seriesName,
				type: type,
				data: config.seriesData
			}		
			]
	};
	
	if(config.series.length>0){
		option.series=config.series;
	}

	return option;
}

//饼图option
function getOptionPie(config){
	var option = {
		color:['#f3b743','#44bfca','#00a0e9','#80c269','#2f4554', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3','#c23531'],
	    title : {
	        text: config.title,
	        subtext: config.subtitle,
	        x:config.title_x||'center'
	    },
	    tooltip : {
	        trigger: 'item'
	        ,formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },
	    legend: {
	        orient : 'vertical',
	        x : config.legend_x||'left',
	        data:config.legend
	    },
	    toolbox: {
            x:100,
            y:0,
            show : true,
            feature : {
                //dataView : {show: true, readOnly: true},
               // magicType : {show: true, type: ['bar','line']},
                //restore : {show: true},
                //saveAsImage : {show: true},
//	              myTool1: {
//		              show: true,
//		              title: '柱状图切换',
//		              icon: 'image://'+_path+'/static/libs/echarts/img/bar.png',
//		              onclick: function (){
//		              	onSearch('bar');                      	
//		              }
//		          },
//		          myTool2: {
//		              show: true,
//		              title: '折线图切换',
//		              icon: 'image://'+_path+'/static/libs/echarts/img/line.png',
//		              onclick: function (){
//		              	onSearch('line');                      	
//		              }
//		          },
//		          myTool: {
//		              show: true,
//		              title: '饼图切换',
//		              icon: 'image://'+_path+'/static/libs/echarts/img/pie.png',
//		              onclick: function (){
//		              	onSearch('pie');                     	
//		              }
//		          }
            }
        },
	   // calculable : true,
	    series : [
	        {
	            name:config.seriesName,
	            type:'pie',
	            radius : [0,'65%'],//饼图的半径大小
	            center: ['50%', '55%'],//饼图的位置
	            data:config.seriesPieData
	        }
	    ]
	}; 
	return option;
}

//折叠柱状图
function getOptionStack(config){
	//堆叠柱状图 
	var option = {
		color:config.color,
		title : {
	        text: config.title,
	        subtext: config.subtitle,
	        x:config.title_x||'left'
	    },
	    toolbox: {
            // x:100,
            // y:0,
             show : true,
             feature : {
                 dataView : {show: true, readOnly: true},
                 magicType : {show: true, type: ['bar','line']},
                 restore : {show: true},
                saveAsImage : {show: true},
             }
         },
	    tooltip : {
	        trigger: 'axis',
	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        }
	    },
	    legend: {
	        data:config.legend,
	        x:config.legend_x||'center'
	    },
	    xAxis :  {
            type : 'category',
            name : config.xName,
            axisTick: {
				alignWithLabel: config.isAlignWithLabel
			},
            data : config.xAxis
        },
	    yAxis : {
            type : 'value',
            name:config.yName
        },
	    series : [	        
	        {
	            name:config.seriesName,
	            barGap: 0,
	            type:'bar',
	            data:config.seriesData
	        },
	        {
	            name:config.stackName,
	            type:'bar',
	            stack: config.stack,
	            data:config.stackData
	        },	      	       
	        {
	        	name:config.parallelName,
	        	type:'bar',
	        	stack: config.parallel,
	        	data:config.parallelData
	        }	      	       
	    ]
	};
	if(config.series.length>0){
		option.series=config.series;
	}
	
	return option;
}

//仪表盘
function getOptionGuage(config){
var app = {};
var option = {
  title: {
    	text: config.title,
    	subtext:config.subtitle,
		x:config.title_x||'left'
    },
  tooltip : {
      formatter: "{a} <br/>{c} {b}"
  },
  toolbox: {
      show: true,
      feature: {
          restore: {show: true},
          saveAsImage: {show: true}
      }
  },
  series : [
      {
          name: 'UPH',
          type: 'gauge',
          z: 3,
          min: 0,
          max: 220,
          splitNumber: 11,
          center: ['60%', '55%'],
          radius: '78%',
          axisLine: {            // 坐标轴线
              lineStyle: {       // 属性lineStyle控制线条样式
                  width: 10
              }
          },
          axisTick: {            // 坐标轴小标记
              length: 15,        // 属性length控制线长
              lineStyle: {       // 属性lineStyle控制线条样式
                  color: 'auto'
              }
          },
          splitLine: {           // 分隔线
              length: 20,         // 属性length控制线长
              lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
                  color: 'auto'
              }
          },
          axisLabel: {
              backgroundColor: 'auto',
              borderRadius: 2,
              color: '#eee',
              padding: 3,
              textShadowBlur: 2,
              textShadowOffsetX: 1,
              textShadowOffsetY: 1,
              textShadowColor: '#222'
          },
          title : {
              // 其余属性默认使用全局文本样式，详见TEXTSTYLE
              fontWeight: 'bolder',
              fontSize: 13,
              fontStyle: 'italic'
          },
          detail : {
              // 其余属性默认使用全局文本样式，详见TEXTSTYLE
              formatter: function (value) {
                  value = (value + '').split('.');
                  value.length < 2 && (value.push('00'));
                  return ('00' + value[0]).slice(-2)
                      + '.' + (value[1] + '00').slice(0, 2);
              },
              fontWeight: '300',
              borderRadius: 2,
              backgroundColor: '#3398DB',
              borderColor: '#aaa',
              shadowBlur: 5,
              shadowColor: '#333',
              shadowOffsetX: 0,
              shadowOffsetY: 3,
              borderWidth: 2,
              textBorderColor: '#000',
              textBorderWidth: 2,
              textShadowBlur: 2,
              textShadowColor: '#fff',
              textShadowOffsetX: 0,
              textShadowOffsetY: 0,
              fontFamily: 'Arial',
              width: 70,
              color: '#eee',
              rich: {}
          },
          data:[{value: 60, name: 'Unit/H'}]
      },
      {
          name: '生产率',
          type: 'gauge',
          center: ['20%', '55%'],    // 默认全局居中
          radius: '35%',
          min:0,
          max:7,
          endAngle:45,
          splitNumber:7,
          axisLine: {            // 坐标轴线
              lineStyle: {       // 属性lineStyle控制线条样式
                  width: 8
              }
          },
          axisTick: {            // 坐标轴小标记
              length:12,        // 属性length控制线长
              lineStyle: {       // 属性lineStyle控制线条样式
                  color: 'auto'
              }
          },
          splitLine: {           // 分隔线
              length:20,         // 属性length控制线长
              lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
                  color: 'auto'
              }
          },
          pointer: {
              width:5
          },
          title: {
              offsetCenter: [0, '-30%'],       // x, y，单位px
          },
          detail: {
              // 其余属性默认使用全局文本样式，详见TEXTSTYLE
              fontWeight: 'bolder'
          },
          data:[{value: 1.5, name: 'pcs/M'}]
      }
//      ,
//      {
//          name: '油表',
//          type: 'gauge',
//          center: ['77%', '50%'],    // 默认全局居中
//          radius: '25%',
//          min: 0,
//          max: 2,
//          startAngle: 135,
//          endAngle: 45,
//          splitNumber: 2,
//          axisLine: {            // 坐标轴线
//              lineStyle: {       // 属性lineStyle控制线条样式
//                  width: 8
//              }
//          },
//          axisTick: {            // 坐标轴小标记
//              splitNumber: 5,
//              length: 10,        // 属性length控制线长
//              lineStyle: {        // 属性lineStyle控制线条样式
//                  color: 'auto'
//              }
//          },
//          axisLabel: {
//              formatter:function(v){
//                  switch (v + '') {
//                      case '0' : return 'E';
//                      case '1' : return 'Gas';
//                      case '2' : return 'F';
//                  }
//              }
//          },
//          splitLine: {           // 分隔线
//              length: 15,         // 属性length控制线长
//              lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
//                  color: 'auto'
//              }
//          },
//          pointer: {
//              width:2
//          },
//          title : {
//              show: false
//          },
//          detail : {
//              show: false
//          },
//          data:[{value: 0.5, name: 'gas'}]
//      },
//      {
//          name: '水表',
//          type: 'gauge',
//          center : ['77%', '50%'],    // 默认全局居中
//          radius : '25%',
//          min: 0,
//          max: 2,
//          startAngle: 315,
//          endAngle: 225,
//          splitNumber: 2,
//          axisLine: {            // 坐标轴线
//              lineStyle: {       // 属性lineStyle控制线条样式
//                  width: 8
//              }
//          },
//          axisTick: {            // 坐标轴小标记
//              show: false
//          },
//          axisLabel: {
//              formatter:function(v){
//                  switch (v + '') {
//                      case '0' : return 'H';
//                      case '1' : return 'Water';
//                      case '2' : return 'C';
//                  }
//              }
//          },
//          splitLine: {           // 分隔线
//              length: 15,         // 属性length控制线长
//              lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
//                  color: 'auto'
//              }
//          },
//          pointer: {
//              width:2
//          },
//          title: {
//              show: false
//          },
//          detail: {
//              show: false
//          },
//          data:[{value: 0.5, name: 'gas'}]
//      }
  ]
};
setInterval(function (){
	  option.series[0].data[0].value = (Math.random()*100).toFixed(2) - 0;
	  option.series[1].data[0].value = (Math.random()*7).toFixed(2) - 0;
//	  option.series[2].data[0].value = (Math.random()*2).toFixed(2) - 0;
//	  option.series[3].data[0].value = (Math.random()*2).toFixed(2) - 0;
	  echarts.init(document.getElementById("gauge")).setOption(option,true);
	},2000);;
return option;
}

//雷达图
function getOptionRader(config){
	var app = {};
	var option = {
		    title: {
		    	text: config.title,
		    	subtext:config.subtitle,
				x:config.title_x||'left'
		    },
		    legend: {
		        data: ['生产力','效率'],
				x:config.legend_x||'center',
				y:config.legend_y||'top'
		    },
		    radar: [
		        {
		            indicator: [
		                { text: '指标一' },
		                { text: '指标二' },
		                { text: '指标三' },
		                { text: '指标四' },
		                { text: '指标五' },
		                { text: '指标六' }
		            ],
		            radius : [2,'40%'],//饼图的半径大小
		            center: ['50%', '50%'],//饼图的位置
		            startAngle: 5,
		            splitNumber: 4,
		            shape: 'circle',
		            name: {
		                formatter:'【{value}】',
		                textStyle: {
		                    color:'#3398DB'
		                }
		            },
		            splitArea: {
		                areaStyle: {
		                    color: ['#4cabce'],
		                    shadowColor: 'rgba(0, 0, 0, 0.3)',
		                    shadowBlur: 10
		                }
		            },
		            axisLine: {
		                lineStyle: {
		                    color: 'rgba(255, 255, 255, 0.5)'
		                }
		            },
		            splitLine: {
		                lineStyle: {
		                    color: 'rgba(255, 255, 255, 0.5)'
		                }
		            }
		        }
		    ],
		    series: [
		        {
		            name: '雷达图',
		            type: 'radar',
		            itemStyle: {
		                emphasis: {
		                    // color: 各异,
		                    lineStyle: {
		                        width: 4
		                    }
		                }
		            },
		            data: [
		                {
		                    value: [100, 8, 0.40, -80, 2000],
		                    name: '生产力',
		                    symbol: 'rect',
		                    symbolSize: 5,
		                    lineStyle: {
		                        normal: {
		                            type: 'dashed',
		                            color: '#f3b743'
		                        }
		                    }
		                },
		                {
		                    value: [60, 5, 0.30, -100, 1500],
		                    name: '效率',
		                    areaStyle: {
		                        normal: {
		                            color: 'rgba(255, 255, 255, 0.5)'
		                        }
		                    }
		                }
		            ]
		        }
		    ]
		}
	return option;
}
//初始化对象
var echart=new Echart();
