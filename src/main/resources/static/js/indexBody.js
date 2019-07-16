var curDelayNumMainEcharts;
var curDelayNumMainOption;
var curDelayNumMainData = [];

var curReadyNumMainEcharts;
var curReadyNumMainOption;
var curReadyNumMainData = [];

var curSuccessNumMainEcharts;
var curSuccessNumMainOption;
var curSuccessNumMainData = [];

var curFailedNumMainEcharts;
var curFailedNumMainOption;
var curFailedNumMainData = [];

/**
 * 数据格式化
 * 格式化之前：{time: "10:20:30", num: 100}
 * 格式化之后：{
            name: '10:20:30',
            value: [
                '10:20:30',
                100
            ]
        }
 * @param data
 * @returns {{name: string, value: any[]}}
 */
function formatData(data) {
    return {
        name: data.time,
        value: [
            data.time,
            data.num
        ]
    }
}

// 报表统计JS
function dailyVisits(data) {
    var length = data.length;
    if (length <= 0) {
        return;
    }
    var xAxisData = [];
    var seriesForVisit = [];
    var seriesForDelayed = [];
    var seriesForReady = [];
    var seriesForSuccess = [];
    var seriesForFailed = [];
    for (var i = 0; i < length; i++) {
        xAxisData.push(data[i].time);
        seriesForVisit.push(data[i].visit);
        seriesForDelayed.push(data[i].delayed);
        seriesForReady.push(data[i].ready);
        seriesForSuccess.push(data[i].success);
        seriesForFailed.push(data[i].failed);
    }
    dailyVisitsMainOption = {
        title: {
            text: '报表统计'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['日访问量', '延迟数', '读取数', '成功数', '失败数']
        },
        // grid: {
        //     left: '3%',
        //     right: '4%',
        //     bottom: '3%',
        //     containLabel: true
        // },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xAxisData
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name: '日访问量',
                type: 'line',
                stack: '总量',
                data: seriesForVisit
            },
            {
                name: '延迟数',
                type: 'line',
                stack: '总量',
                data: seriesForDelayed
            },
            {
                name: '读取数',
                type: 'line',
                stack: '总量',
                data: seriesForReady
            },
            {
                name: '成功数',
                type: 'line',
                stack: '总量',
                data: seriesForSuccess
            },
            {
                name: '失败数',
                type: 'line',
                stack: '总量',
                data: seriesForFailed
            }
        ]
    };
    var dailyVisitsMain = echarts.init(document.getElementById('dailyVisitsMain'));
    dailyVisitsMain.setOption(dailyVisitsMainOption);
}


<!--今日延迟数JS-->
function curDelay(data) {
    // 初始化历史数据
    curDelayNumMainEcharts = echarts.init(document.getElementById('curDelayNumMain'));
    var length = data.length;
    for (var i = 0; i < length; i++) {
        curDelayNumMainData.push(formatData(data[i]));
    }
    curDelayNumMainOption = {
        title: {
            text: '今日延迟数',
            x: 'center',
            textAlign: 'center'
        },
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                params = params[0];
                return "[" + params.name + "]" + ' -> ' + params.value[1];
            },
            axisPointer: {
                animation: false
            }
        },
        xAxis: {
            // type: 'time',
            type: 'category',
            splitLine: {
                show: false,
                name: '时间',
                nameLocation: 'end'
            }
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, '100%'],
            splitLine: {
                show: false
            }
        },
        series: [{
            name: 'data',
            type: 'line',
            showSymbol: false,
            hoverAnimation: false,
            data: curDelayNumMainData
        }]
    };
    curDelayNumMainEcharts.setOption(curDelayNumMainOption);
}

<!--今日读取数JS-->
function curReady(data) {
    curReadyNumMainEcharts = echarts.init(document.getElementById('curReadyNumMain'));
    var length = data.length;
    for (var i = 0; i < length; i++) {
        curReadyNumMainData.push(formatData(data[i]));
    }
    curReadyNumMainOption = {
        title: {
            text: '今日读取数',
            x: 'center',
            textAlign: 'center'
        },
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                params = params[0];
                return "[" + params.name + "]" + ' -> ' + params.value[1];
            },
            axisPointer: {
                animation: false
            }
        },
        xAxis: {
            // type: 'time',
            type: 'category',
            splitLine: {
                show: false,
                name: '时间',
                nameLocation: 'end'
            }
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, '100%'],
            splitLine: {
                show: false
            }
        },
        series: [{
            name: 'data',
            type: 'line',
            showSymbol: false,
            hoverAnimation: false,
            data: curReadyNumMainData
        }]
    };
    curReadyNumMainEcharts.setOption(curReadyNumMainOption);
}


<!--今日成功数JS-->
function curSuccess(data) {
    curSuccessNumMainEcharts = echarts.init(document.getElementById('curSuccessNumMain'));
    var length = data.length;
    for (var i = 0; i < length; i++) {
        curSuccessNumMainData.push(formatData(data[i]));
    }
    curSuccessNumMainOption = {
        title: {
            text: '今日成功数',
            x: 'center',
            textAlign: 'center'
        },
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                params = params[0];
                return "[" + params.name + "]" + ' -> ' + params.value[1];
            },
            axisPointer: {
                animation: false
            }
        },
        xAxis: {
            // type: 'time',
            type: 'category',
            splitLine: {
                show: false,
                name: '时间',
                nameLocation: 'end'
            }
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, '100%'],
            splitLine: {
                show: false
            }
        },
        series: [{
            name: 'data',
            type: 'line',
            showSymbol: false,
            hoverAnimation: false,
            data: curSuccessNumMainData
        }]
    };
    curSuccessNumMainEcharts.setOption(curSuccessNumMainOption);
}


<!--今日失败数JS-->
function curFailed(data) {
    curFailedNumMainEcharts = echarts.init(document.getElementById('curFailedNumMain'));
    var length = data.length;
    for (var i = 0; i < length; i++) {
        curFailedNumMainData.push(formatData(data[i]));
    }
    curFailedNumMainOption = {
        title: {
            text: '今日失败数',
            x: 'center',
            textAlign: 'center'
        },
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                params = params[0];
                return "[" + params.name + "]" + ' -> ' + params.value[1];
            },
            axisPointer: {
                animation: false
            }
        },
        xAxis: {
            // type: 'time',
            type: 'category',
            splitLine: {
                show: false,
                name: '时间',
                nameLocation: 'end'
            }
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, '100%'],
            splitLine: {
                show: false
            }
        },
        series: [{
            name: 'data',
            type: 'line',
            showSymbol: false,
            hoverAnimation: false,
            data: curFailedNumMainData
        }]
    };
    curFailedNumMainEcharts.setOption(curFailedNumMainOption);
}

/**
 * 请求首页数据
 */
function ajaxRequestAdminIndex() {
    $.get("/admin/index", function (result) {
        if (0 !== result.code) {
            return;
        }

        var visitNumberForTodayDoc = $("#visitNumberForToday");
        var visitNumberForTodayVal = result.data.curVisitNumber;
        var visitNumberForTodayNum = 1;
        var visitNumberForTodayT = setInterval(function () {
            visitNumberForTodayNum += visitNumberForTodayNum + 1;
            visitNumberForTodayDoc.html(visitNumberForTodayNum);
            if (visitNumberForTodayNum > visitNumberForTodayVal) {
                visitNumberForTodayDoc.html(visitNumberForTodayVal);
                clearInterval(visitNumberForTodayT);
            }
        }, 20);
        var visitNumberForTotalDoc = $("#visitNumberForTotal");
        visitNumberForTotalDoc.html(result.data.visitNumberTotal);


        var curDelayedDoc = $("#curDelayed");
        var curDelayedVal = result.data.curDelayed;
        var curDelayedNum = 1;
        var curDelayedT = setInterval(function () {
            curDelayedNum += curDelayedNum + 1;
            curDelayedDoc.html(curDelayedNum);
            if (curDelayedNum > curDelayedVal) {
                curDelayedDoc.html(curDelayedVal);
                clearInterval(curDelayedT);
            }
        }, 20);
        var curDelayedTotalDoc = $("#curDelayedTotal");
        curDelayedTotalDoc.html(result.data.curDelayedTotal);


        var curReadyDoc = $("#curReady");
        var curReadyVal = result.data.curReady;
        var curReadyNum = 1;
        var curReadyT = setInterval(function () {
            curReadyNum += curReadyNum + 1;
            curReadyDoc.html(curReadyNum);
            if (curReadyNum > curReadyVal) {
                curReadyDoc.html(curReadyVal);
                clearInterval(curReadyT);
            }
        }, 20);
        var curReadyTotalDoc = $("#curReadyTotal");
        curReadyTotalDoc.html(result.data.curReadyTotal);


        var curFailedDoc = $("#curFailed");
        var curFailedVal = result.data.curFailed;
        var curFailedNum = 1;
        var curFailedT = setInterval(function () {
            curFailedNum += curFailedNum + 1;
            curFailedDoc.html(curFailedNum);
            if (curFailedNum > curFailedVal) {
                curFailedDoc.html(curFailedVal);
                clearInterval(curFailedT);
            }
        }, 20);
        var curFailedTotalDoc = $("#curFailedTotal");
        curFailedTotalDoc.html(result.data.curFailedTotal);

        // 渲染统计报表
        dailyVisits(result.data.historyData);

        // 渲染历史数据
        $.get("/admin/realTimeMonitorHistory", function (result) {
            console.info(111);
            console.info(result);
            if (0 !== result.code) {
                return;
            }

            var dataList = result.data;
            var length = dataList.length;
            if (length < 1) {
                // todo 没有数据
                return;
            }
            for (var i = 0; i < length; i++) {
                var dataElement = dataList[i];
                if ("DELAYED" === dataElement.type) {
                    // 渲染今日延迟数
                    curDelay(dataElement.monitorList);
                } else if ("READY" === dataElement.type) {
                    // 渲染今日读取数
                    curReady(dataElement.monitorList);
                } else if ("SUCCESS" === dataElement.type) {
                    // 渲染今日成功数
                    curSuccess(dataElement.monitorList);
                } else if ("FAILED" === dataElement.type) {
                    // 渲染今日失败数
                    curFailed(dataElement.monitorList);
                }
            }
        });
    });
}

/**
 * 定时器
 */
indexT = setInterval(function () {
    $.get("/admin/realTimeMonitor", function (result) {
        if (0 !== result.code) {
            return;
        }
        var dataList = result.data;
        var length = dataList.length;
        if (length < 1) {
            // todo 没有数据
            return;
        }
        for (var i = 0; i < length; i++) {
            var dataElement = dataList[i];
            if ("DELAYED" === dataElement.type) {
                curDelayNumMainData.shift();
                curDelayNumMainData.push(formatData(dataElement.monitorList[0]));
                curDelayNumMainEcharts.setOption({
                    series: [{
                        data: curDelayNumMainData
                    }]
                });
            } else if ("READY" === dataElement.type) {
                curReadyNumMainData.shift();
                curReadyNumMainData.push(formatData(dataElement.monitorList[0]));
                curReadyNumMainEcharts.setOption({
                    series: [{
                        data: curReadyNumMainData
                    }]
                });
            } else if ("SUCCESS" === dataElement.type) {
                curSuccessNumMainData.shift();
                curSuccessNumMainData.push(formatData(dataElement.monitorList[0]));
                curSuccessNumMainEcharts.setOption({
                    series: [{
                        data: curSuccessNumMainData
                    }]
                });
            } else if ("FAILED" === dataElement.type) {
                curFailedNumMainData.shift();
                curFailedNumMainData.push(formatData(dataElement.monitorList[0]));
                curFailedNumMainEcharts.setOption({
                    series: [{
                        data: curFailedNumMainData
                    }]
                });
            }
        }
    });
}, 5000);

