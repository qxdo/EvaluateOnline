// Example Bootstrap Table Events
(function() {
	$('#teacherList').bootstrapTable({
		url: "/admin/teacher/list",
		search: true,
		pagination: true,
		showRefresh: true,
		showToggle: true,
		showColumns: true,
		iconSize: 'outline',
		toolbar: '#teacherToolbar',
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
	$('#teacherList').bootstrapTable('hideColumn', 'sex');
	$('#teacherList').bootstrapTable('hideColumn', 'id');
})();

/*表格行增加删除函数*/
function actionFormatter(value, row, index) {
	return '<a class="mod" >修改</a> ' + '<a class="delete">删除</a>'+ '&nbsp;&nbsp;<a class="reset">重置</a>';
}
//教师实验修改权限
function actionTestFormatter(value, row, index) {
	if(row.status == 1) {
		return '<a class="testpower">允许修改</a> ';
	} else {
		return '<a class="testpower">禁止修改</a> ';
	}
}
//性别判断显示中文男女
function actionrsexFormatter(value, row, index) {
	if(row.sex == "1") {
		return '男 ';
	} else {
		return '女';
	}
}
//表格  - 操作 - 事件
window.actionEvents = {
	'click .mod': function(e, value, row, index) { //修改
		//修改操作
		/*console.log("工号：" + row.column1);
		location.href = "teacher_update.html"*/
		var id = row.id;
		var number = row.number;

		document.location.href = "/admin/teacher/update?number=" + number+"&id="+id;
	},
	'click .delete': function(e, value, row, index) { //删除
		
		if(!confirm("确认要删除吗？")) {
			return;
		}
		var id = row.id;
		console.log(id);
		var number = row.number;

		$.ajax({
			type: "POST",
			url: "/admin/teacher/delete",
			data: {
				"number": number,
				"id": id
			},
			dataType: "JSON",
			success: function(data) {
				if(data.code == 1) {
					//删除成功
					$("#success").show();
					$("#teacherList").bootstrapTable('remove', {
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
	'click .reset': function(e, value, row, index) {//重置
		var id = row.id;
		$.ajax({
			type: "post",
			url: "/admin/reset",
			data: {id:id,usertype: 'teacher'},
			dataType: "JSON",
			success:function(data){
				if(data.code == 1){
					//重置成功
					$("#resetsuccess").show();
					setTimeout(function(){
						$("#resetsuccess").hide();
					},2000);
				}else{
					//重置失败
					$("#resetfail").show();
					setTimeout(function(){
						$("#resetfail").hide();
					},2000);
				}
			}
		});
	}
}
window.actionTestEvents = {
		'click .testpower': function(e, value, row, index) {
			var tr = this.parentNode.parentNode;
			var cells = tr.cells;
			/*console.log("cells------------------"+cells[0].innerHTML+","+cells[1].innerHTML+","+cells[2].innerHTML+","+cells[3].innerHTML+","+cells[4].innerHTML+","+cells[5].innerHTML+","+cells[6].innerHTML+","+cells[7].innerHTML);
			console.log(cells[7].childNodes[0].innerHTML);*/
			var teacherAuthText = cells[7].childNodes[0];
			if(teacherAuthText.innerHTML == "禁止修改") {
				teacherAuthText.innerHTML = "允许修改";
				/*console.log(teacherAuthText.innerHTML);*/
			} else {
				teacherAuthText.innerHTML = "禁止修改";
				/*console.log(teacherAuthText.innerHTML);*/
			}

		}
	}
	/*增加分配函数*/
function distribFormatter(value, row, index) {
	return '<a class="distrib">分配</a> ';
}
//分配
window.distribEvents = {
	'click .distrib': function(e, value, row, index) {
		var id = row.id;
		location.href = "distribute.html?type=2&id=" + id;
	}
}