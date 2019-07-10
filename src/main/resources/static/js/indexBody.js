// 报表统计JS
function dailyVisits(data) {
    console.info(data);
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
        // todo 成功数的问题
        seriesForSuccess.push(data[i].delayed);
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
            // data: ['2019/07/01', '2019/07/02', '2019/07/03', '2019/07/04', '2019/07/05', '2019/07/06', '2019/07/07', '2019/07/08']
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
                // data: [120, 132, 101, 134, 90, 230, 2170, 100]
                data: seriesForVisit
            },
            {
                name: '延迟数',
                type: 'line',
                stack: '总量',
                // data: [220, 182, 191, 234, 290, 330, 310, 200]
                data: seriesForDelayed
            },
            {
                name: '读取数',
                type: 'line',
                stack: '总量',
                // data: [150, 232, 201, 154, 190, 330, 2410, 300]
                data: seriesForReady
            },
            {
                name: '成功数',
                type: 'line',
                stack: '总量',
                // data: [820, 932, 901, 934, 1290, 1330, 1320, 500]
                data: seriesForSuccess
            },
            {
                name: '失败数',
                type: 'line',
                stack: '总量',
                // data: [320, 332, 301, 334, 390, 330, 320, 400]
                data: seriesForFailed
            }
        ]
    };
    var dailyVisitsMain = echarts.init(document.getElementById('dailyVisitsMain'));
    dailyVisitsMain.setOption(dailyVisitsMainOption);
}


<!--今日延迟数JS-->
function curDelay() {
    var curDelayNumMainEcharts = echarts.init(document.getElementById('curDelayNumMain'));

    function randomData() {
        curDelayNumMainNow = new Date(+curDelayNumMainNow + curDelayNumMainS5);
        curDelayNumMainValue = curDelayNumMainValue + Math.random() * 21 - 10;
        var time = [curDelayNumMainNow.getHours(), curDelayNumMainNow.getMinutes(), curDelayNumMainNow.getSeconds()].join(':');
        return {
            name: time,
            value: [
                time,
                Math.round(curDelayNumMainValue)
            ]
        }
    }

    var curDelayNumMainData = [];
    var curDelayNumMainNow = new Date();
    var curDelayNumMainS5 = 1000 * 5;
    var curDelayNumMainValue = Math.random() * 1000;
    for (var i = 0; i < 1000; i++) {
        curDelayNumMainData.push(randomData());
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
    curDelayNumMainEcharts.setOption({
        series: [{
            data: curDelayNumMainData
        }]
    });

    setInterval(function () {

        curDelayNumMainData.shift();
        curDelayNumMainData.push(randomData());

        curDelayNumMainEcharts.setOption(curDelayNumMainOption);
        curDelayNumMainEcharts.setOption({
            series: [{
                data: curDelayNumMainData
            }]
        });
    }, 5000);
}

<!--今日读取数JS-->
function curReady() {
    var curReadyNumMainEcharts = echarts.init(document.getElementById('curReadyNumMain'));

    function randomData() {
        curReadyNumMainNow = new Date(+curReadyNumMainNow + curReadyNumMainS5);
        curReadyNumMainValue = curReadyNumMainValue + Math.random() * 21 - 10;
        var time = [curReadyNumMainNow.getHours(), curReadyNumMainNow.getMinutes(), curReadyNumMainNow.getSeconds()].join(':');
        return {
            name: time,
            value: [
                time,
                Math.round(curReadyNumMainValue)
            ]
        }
    }

    var curReadyNumMainData = [];
    var curReadyNumMainNow = new Date();
    var curReadyNumMainS5 = 1000 * 5;
    var curReadyNumMainValue = Math.random() * 1000;
    for (var i = 0; i < 1000; i++) {
        curReadyNumMainData.push(randomData());
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
    curReadyNumMainEcharts.setOption({
        series: [{
            data: curReadyNumMainData
        }]
    });

    setInterval(function () {

        curReadyNumMainData.shift();
        curReadyNumMainData.push(randomData());

        curReadyNumMainEcharts.setOption(curReadyNumMainOption);
        curReadyNumMainEcharts.setOption({
            series: [{
                data: curReadyNumMainData
            }]
        });
    }, 5000);
}


<!--今日成功数JS-->
function curSuccess() {

    var curSuccessNumMainEcharts = echarts.init(document.getElementById('curSuccessNumMain'));

    function randomData() {
        curSuccessNumMainNow = new Date(+curSuccessNumMainNow + curSuccessNumMainS5);
        curSuccessNumMainValue = curSuccessNumMainValue + Math.random() * 21 - 10;
        var time = [curSuccessNumMainNow.getHours(), curSuccessNumMainNow.getMinutes(), curSuccessNumMainNow.getSeconds()].join(':');
        return {
            name: time,
            value: [
                time,
                Math.round(curSuccessNumMainValue)
            ]
        }
    }

    var curSuccessNumMainData = [];
    var curSuccessNumMainNow = new Date();
    var curSuccessNumMainS5 = 1000 * 5;
    var curSuccessNumMainValue = Math.random() * 1000;
    for (var i = 0; i < 1000; i++) {
        curSuccessNumMainData.push(randomData());
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
    curSuccessNumMainEcharts.setOption({
        series: [{
            data: curSuccessNumMainData
        }]
    });

    setInterval(function () {

        curSuccessNumMainData.shift();
        curSuccessNumMainData.push(randomData());

        curSuccessNumMainEcharts.setOption(curSuccessNumMainOption);
        curSuccessNumMainEcharts.setOption({
            series: [{
                data: curSuccessNumMainData
            }]
        });
    }, 5000);
}


<!--今日失败数JS-->
function curFailed() {

    var curFailedNumMainEcharts = echarts.init(document.getElementById('curFailedNumMain'));

    function randomData() {
        curFailedNumMainNow = new Date(+curFailedNumMainNow + curFailedNumMainS5);
        curFailedNumMainValue = curFailedNumMainValue + Math.random() * 21 - 10;
        var time = [curFailedNumMainNow.getHours(), curFailedNumMainNow.getMinutes(), curFailedNumMainNow.getSeconds()].join(':');
        return {
            name: time,
            value: [
                time,
                Math.round(curFailedNumMainValue)
            ]
        }
    }

    var curFailedNumMainData = [];
    var curFailedNumMainNow = new Date();
    var curFailedNumMainS5 = 1000 * 5;
    var curFailedNumMainValue = Math.random() * 1000;
    for (var i = 0; i < 1000; i++) {
        curFailedNumMainData.push(randomData());
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
    curFailedNumMainEcharts.setOption({
        series: [{
            data: curFailedNumMainData
        }]
    });

    setInterval(function () {

        curFailedNumMainData.shift();
        curFailedNumMainData.push(randomData());

        curFailedNumMainEcharts.setOption(curFailedNumMainOption);
        curFailedNumMainEcharts.setOption({
            series: [{
                data: curFailedNumMainData
            }]
        });
    }, 5000);
}

/**
 * 请求首页数据
 */
function ajaxRequestAdminIndex() {
    $.get("/admin/index", function (result) {
        if (0 !== result.code) {
            return;
        }
        console.info(result);

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

        // 渲染今日延迟数
        curDelay();

        // 渲染今日读取数
        curReady();

        // 渲染今日成功数
        curSuccess();

        // 渲染今日失败数
        curFailed();
    });
}