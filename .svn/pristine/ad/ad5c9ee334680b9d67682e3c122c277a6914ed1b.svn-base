
  function setRadioDefault(name, value) {
    $('[name="' + name + '"]:radio').each(function() {
      if (this.value == value){
        this.checked = true;
      }
    });
  }

  function copy(o) {
    var copy = Object.create(Object.getPrototypeOf(o));
    var propNames = Object.getOwnPropertyNames(o);
  
    propNames.forEach(function(name) {
      var desc = Object.getOwnPropertyDescriptor(o, name);
      Object.defineProperty(copy, name, desc);
    });
  
    return copy;
  }
  
  /**
   * 扩展数组remove方法
   */
  Array.prototype.remove = function(el){  
    return this.splice(this.indexOf(el),1);  
  }

  //------------------格式化时间为 yyyy-MM-dd ---------------------------------------  
  $.fn.datebox.defaults.formatter = function(date) {  
	  var y = date.getFullYear();  
	  var m = date.getMonth() + 1;  
	  var d = date.getDate();  
	  return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d);  
  };  
  //  
  $.fn.datebox.defaults.parser = function(s) {
      var t = Date.parse(s.replace(/-/g, '/'));
      if (!isNaN(t)){
          return new Date(t);
      } else {
          return new Date();
      }
  };  