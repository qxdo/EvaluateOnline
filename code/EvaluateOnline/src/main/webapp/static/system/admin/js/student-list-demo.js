// Example Bootstrap Table Events
(function() {
	$('#studentList').bootstrapTable({
		url: "/admin/student/list",/*bootstrap_table_test.json*/
		search: true,
		pagination: true,
		showRefresh: true,
		showToggle: true,
		showColumns: true,
		iconSize: 'outline',
		toolbar: '#studentToolbar',
		icons: {
			refresh: 'glyphicon-repeat',
			toggle: 'glyphicon-list-alt',
			columns: 'glyphicon-list'
		},
		columns:[{  
	            field: 'listnumber',//可不加  
	            title: '序号',//标题  可不加  
	            formatter: function (value, row, index) {  
	                return index+1; 
	            }  
	        },{  
	            checkbox: false 
        	}]  
	});
	$('#studentList').bootstrapTable('hideColumn', 'sex');
	$('#studentList').bootstrapTable('hideColumn', 'id');
})();
/*表格行增加删除函数*/
function actionFormatter(value, row, index) {
	return '<a class="mod" >修改</a> ' + '<a class="delete">删除</a>'+ '&nbsp;&nbsp;<a class="reset">重置</a>';
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
	'click .mod': function(e, value, row, index) {//修改
		//修改操作
		/*alert("工号：" + row.column1);
		location.href = "student_update.html"*/
		var id = row.id;
		var number = row.number;
		
		document.location.href = "/admin/student/update?number="+number+"&id="+id;
	},
	'click .delete': function(e, value, row, index) {//删除
		
		if(!confirm("确认要删除吗？")) {
			return;
		}
		var id = row.id;
		var number = row.number;
		
		$.ajax({
    		type: "POST",
			url: "/admin/student/delete",
			data: {number: number,id:id},
			dataType: "JSON",
			success:function(data){
				if(data.code==1){
                    //删除成功
                    $("#success").show();
                    $("#studentList").bootstrapTable('remove', {
						field: 'id',
						values: [parseInt(id)]
					});
					setTimeout(function(){
						$("#success").hide();
					},2000);
				}else{
                   $("#fail").show();
					setTimeout(function(){
						$("#fail").hide();
					},2000);
				}
			}
    	});
	},
	'click .reset': function(e, value, row, index) {//重置
		var id = row.id;
		$.ajax({
			type: "post",
			url: "/admin/reset",
			data: {id:id,usertype: 'student'},
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
