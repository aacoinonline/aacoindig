;(function($){
	var opts = {}, ids = [], optsArray = [], that, currentOpts = {};
	
	var pcData = {'provinces':[]},//省市数据
		caData = {'cities':[]};//市区数据
	var defaultStr = '<a href="javascript:void(0);" val="-1">请选择</a>',
		defaultId = -1,
		defaultName = '请选择';
	
	//处理取回的数据
	function handleData(datas){
		var tempArr = [];
		var tempProvinces = [];
		var tempCities = [];
		var tempAreas = [];
		var reProvince=/^[0-9]{2}0{4}$/;//省的格式:前2位为'01~99'，后4位为'0000'  例：010000
		var reCity=/^[0-9]{4}0{2}$/;//城市的格式:前2位为'01~99',中间2位'01~99',后2位为'00' 例:010100
		var reArea=/^[0-9]{6}$/;//区的格式:前2位为'01~99',中间2位'01~99',后2位为'01~99' 例:010101
		
		//循环取得的数据，添加到用于存放省、市、区的临时数组中
		for(var i=0; i<datas.length; i++){
			tempArr = datas[i].split(':');
			if(reProvince.test(tempArr[0])){
				tempProvinces.push({'id':tempArr[0],'name':tempArr[1], 'cities':[]});
			}else if(reCity.test(tempArr[0])){
				tempCities.push({'id':tempArr[0],'name':tempArr[1], 'areas':[]});
			}else if(reArea.test(tempArr[0])){
				tempAreas.push({'id':tempArr[0],'name':tempArr[1], 'towns':[]});
			}
		}
		
		//将城市信息添加到对应省份中去
		for(var i=0; i<tempProvinces.length; i++){
			var pId = tempProvinces[i].id.substring(0,2);
			for(var j=0; j<tempCities.length; j++){
				if(tempCities[j].id.substring(0,2) == pId){
					tempProvinces[i].cities.push(tempCities[j]);
				}
			}
		}
		
		//将区信息添加到对应城市中去
		for(var i=0; i<tempCities.length; i++){
			var cId = tempCities[i].id.substring(0,4);
			for(var j=0; j<tempAreas.length; j++){
				if(tempAreas[j].id.substring(0,4) == cId){
					tempCities[i].areas.push(tempAreas[j]);
				}
			}
		}
		
		pcData.provinces = tempProvinces;
		caData.cities = tempCities;
		
	};
	
	function showProvince(obj, selectPId){
		var tempStr = defaultStr;
		var tempId = defaultId;
		var tempName = defaultName;
		var _provinces = pcData.provinces;
		var _cities = [];
		if(_provinces.length > 0){
			for(var i=0; i<_provinces.length; i++){
				if(selectPId && selectPId == _provinces[i].id){
					tempStr += '<a href="javascript:void(0);" val="' + _provinces[i].id + '" class="selected">' + _provinces[i].name + '</a>';
					tempId = _provinces[i].id;
					tempName = _provinces[i].name;
					_cities = _provinces[i].cities;
				}else{
					tempStr += '<a href="javascript:void(0);" val="' + _provinces[i].id + '">' + _provinces[i].name + '</a>';
				}
			}
		}
		
		$(obj).find('div[name="province"]').html('<div class="opts">'+ tempStr +'</div');
		$(obj).find('div[name="province"]').inputbox({'width':currentOpts.width, 'height':currentOpts.height});

		return _cities;
	};
	
	function showCity(obj, cities, selectCId){
		var tempStr = defaultStr;
		var tempId = defaultId;
		var tempName = defaultName;
		var _cities = cities? cities : [];
		var _areas = [];

		if(_cities.length > 0){
			for(var i=0; i<_cities.length; i++){
				if(selectCId && selectCId == _cities[i].id){
					tempStr += '<a href="javascript:void(0);" val="' + _cities[i].id + '" class="selected">' + _cities[i].name + '</a>';	
					tempId = _cities[i].id;
					tempName = _cities[i].name;
					_areas = _cities[i].areas;
				}else{
					tempStr += '<a href="javascript:void(0);" val="' + _cities[i].id + '">' + _cities[i].name + '</a>';
				}
			}
		}
		
		$(obj).find('div[name="city"]').html('<div class="opts">'+ tempStr +'</div');
		$(obj).find('div[name="city"]').inputbox({'width':currentOpts.width, 'height':currentOpts.height});
		
		return _areas;
	};
	
	function showArea(obj, areas, selectAId){
		var tempStr = defaultStr;
		var tempId = defaultId;
		var tempName = defaultName
		var _areas = areas? areas : [];
		var _towns = [];
		
		if(_areas.length > 0){
			for(var i=0; i<_areas.length; i++){
				if(selectAId && selectAId == _areas[i].id){
					tempStr += '<a href="javascript:void(0);" val="' + _areas[i].id + '" class="selected">' + _areas[i].name + '</a>';
					tempId = _areas[i].id;
					tempName = _areas[i].name;
					_towns = _areas[i].towns;
				}else{
					tempStr += '<a href="javascript:void(0);" val="' + _areas[i].id + '">' + _areas[i].name + '</a>';
				}
			}
		}
		
		$(obj).find('div[name="area"]').html('<div class="opts">'+ tempStr +'</div');
		$(obj).find('div[name="area"]').inputbox({'width':currentOpts.width, 'height':currentOpts.height});

		return _towns;
	};

	function createInterval(){
		var spid = $(that).find('input[name="province"]').val();
		var scid = $(that).find('input[name="city"]').val();
		var said = $(that).find('input[name="area"]').val();
		var _that = that;
	
		var checkValChange = setInterval(function(){
		
			var _spid = $(_that).find('input[name="province"]').val(),
				_scid = $(_that).find('input[name="city"]').val(),
				_said = $(_that).find('input[name="area"]').val();
				
			if(optsArray.length > 0){
				for(var i=0; i<optsArray.length; i++){
					if(_that == optsArray[i].obj){
						
						currentOpts = optsArray[i].opts;
						
						//监听省份变化，改变城市的值
						if(optsArray[i].ids.spid != _spid){
							optsArray[i].ids.spid = _spid;
							if(_spid == -1){
								showCity(_that);
							}
							for(var j=0; j< pcData.provinces.length; j++){
								if(_spid == pcData.provinces[j].id){
									showCity(_that, pcData.provinces[j].cities);
									break;
								}
							}
							$(_that).find('input[name="area"]').val(-1);
						}
						//监听城市变化，改变区的值
						if(optsArray[i].ids.scid != _scid){
							optsArray[i].ids.scid = _scid;
							if(_scid == -1){
								showArea(_that);
							}
							for(var j=0; j< caData.cities.length; j++){
								if(_scid == caData.cities[j].id){
									showArea(_that, caData.cities[j].areas);
									break;
								}
							}
						}
						
						break;
					}
				}
			}
		},10);	
	};

	function init(){
		handleData(opts.data);
		currentOpts = opts;
		var spid = $(that).find('.province').val();
		var scid = $(that).find('.city').val();
		var said = $(that).find('.area').val();
		
		var ps = showProvince(that, spid);
		var cs = showCity(that, ps, scid);
		var as = showArea(that, cs, said);
		
		optsArray.push({obj:that, opts:opts, ids:{spid:spid, scid:scid, said:said}});
		createInterval();
	};
	
	$.fn.ganged = function(options){
        opts = $.extend({}, $.fn.ganged.defaults, options);

        return this.each(function(){
			that = this;
            init();
        });
    };

    $.fn.ganged .defaults = {
		data : [],
		width : 'auto',
		height : 24
    };
})(jQuery);