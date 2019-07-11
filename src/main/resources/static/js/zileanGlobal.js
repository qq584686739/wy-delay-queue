$("#delayed").click(function () {
    clearInterval(indexT);
    $('#indexBody').load('./html/body/delayedList.html');

    // todo indexBody 切换主体内容，请清除定时器
});
