// Example Bootstrap Table Events
(function() {
	$('#listTableEvents').bootstrapTable({
		url: "bootstrap_table_test.json",
		/*bootstrap_table_test.json*/
		search: true,
		pagination: true,
		showRefresh: true,
		showToggle: true,
		showColumns: true,
		iconSize: 'outline',
		toolbar: '#listToolbar',
		icons: {
			refresh: 'glyphicon-repeat',
			toggle: 'glyphicon-list-alt',
			columns: 'glyphicon-list'
		},
		columns: [{
			field: 'listnumber', //可不加  
			title: '序号', //标题  可不加  
			formatter: function(value, row, index) {
				return index + 1;
			}
		}, {
			checkbox: false
		}]
	});
})();
/*表格行增加删除函数*/
function actionFormatter(value, row, index) {//课程
	return '<a class="mod" >修改</a> ' + '<a class="delete">删除</a>'+ '<a class="dist" >分配</a>';
}
//添加分配列
function distribFormatter(value, row, index) {//教师&班级
	return '<a class="dist" >分配</a> ';
}
//表格  - 操作 - 事件
window.actionEvents = {
	'click .mod': function(e, value, row, index) { //修改   
		//修改操作
		/*alert("工号：" + row.column1);
		location.href="yw_update.html"*/
		var number = row.number;
		console.log(number);

		document.location.href = "/admin/update?number=" + number;
	},
	'click .delete': function(e, value, row, index) { //删除
		var id = row.id;
		if(!confirm("确认要删除吗？")) {
			return;
		}
		var number = row.number;
		console.log(number);

		$.ajax({
			type: "POST",
			url: "/admin/delete",
			data: {
				number: number
			},
			dataType: "JSON",
			success: function(data) {
				if(data.code == 1) {
					//删除成功
					$("#success").show();
					$("#listTableEvents").bootstrapTable('remove', {
						field: 'id',
						values: [parseInt(id)]
					});
					setTimeout(function() {
						$("#success").hide();
					}, 2000);
				} else {
					$("#fail").show();
					setTimeout(function() {
						$("#fail").hide();
					}, 2000);
				}
			}
		});
	},
	'click .dist': function(e, value, row, index) { //分配
		var number = row.number;
		window.location.href = "distribute.html?number="+number;
	}
}
window.distribEvents = {
	'click .dist': function(e, value, row, index) { //分配
		var number = row.number;
		window.location.href = "distribute.html?number="+number;
	}
}
