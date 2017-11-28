// Example Bootstrap Table Events
(function() {
	$('#classList').bootstrapTable({
		url: "/admin/class/list",/*bootstrap_table_test.json*/
		search: true,
		pagination: true,
		showRefresh: true,
		showToggle: true,
		showColumns: true,
		iconSize: 'outline',
		toolbar: '#classToolbar',
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
	/*$('#classList').bootstrapTable('hideColumn', 'id');*/
})();

/*表格行增加删除函数*/
function actionFormatter(value, row, index) {
	return '<a class="mod" >修改</a>' + '&nbsp;&nbsp;<a class="delete">删除</a>';
}

//表格  - 操作 - 事件
window.actionEvents = {
	'click .mod': function(e, value, row, index) {//修改
		//修改操作
		/*location.href = "class_update.html"*/
		var number = row.number;
		console.log(number);
		
		document.location.href = "/admin/class/update?number="+number;
	},
	'click .delete': function(e, value, row, index) {//删除
		
		if(!confirm("确认要删除吗？")) {
			return;
		}
		var id = row.id;
		var number = row.number;
		//console.log(number);
		
		$.ajax({
    		type: "POST",
			url: "/admin/class/delete",
			data: {number: number,id:id},
			dataType: "JSON",
			success:function(data){
				if(data.code==1){
					//删除成功
                    $("#success").show();
                    $("#classList").bootstrapTable('remove', {
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
	}
	
}
/*增加分配函数*/
function distribFormatter(value, row, index) {
	return '<a class="distrib">分配</a> ';
}

window.distribEvents={
	'click .distrib': function(e, value, row, index) {
		var id = row.id;
		location.href="distribute.html?type=2&id="+id;
	}
}
