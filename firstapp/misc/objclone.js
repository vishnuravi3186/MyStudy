/**
 * Created by sunboss on 2014/8/4.
 */
// Shallow Copy
Object.prototype.shallowClone = function() {
    var newObj = {};
    for (var i in this) {
        newObj[i] = this[i];
    }
    return newObj;
}

var obj = {
    name: 'SBS',
    likes: ['node']
};
var newObj = obj.shallowClone();
obj.likes.push('python');
console.log(obj.likes);             // 输出 ['node', 'python']
console.log(newObj.likes);          // 输出 ['node', 'python']

// Deep Copy
Object.prototype.clone = function() {
    var newObj = {};
    for (var i in this) {
        if (typeof(this[i]) == 'object' || typeof(this[i]) == 'function') {
            newObj[i] = this[i].clone();
        } else {
            newObj[i] = this[i];
        }
    }
    return newObj;
};

Array.prototype.clone = function() {
    var newArray = [];
    for (var i = 0; i < this.length; i++) {
        if (typeof(this[i]) == 'object' || typeof(this[i]) == 'function') {
            newArray[i] = this[i].clone();
        } else {
            newArray[i] = this[i];
        }
        return newArray;
    }
};

Function.prototype.clone = function() {
    var that = this;
    var newFunc = function() {
        return that.apply(this, arguments);
    };
    for (var i in this) {
        newFunc[i] = this[i];
    }
    return newFunc;
};

var obj = {
    name: 'SBS',
    likes: ['node'],
    display: function() {
        console.log(this.name);
    }
};

var newObj = obj.clone();
newObj.likes.push('python');
console.log(obj.likes);         // 输出 ['node']
console.log(newObj.likes);      // 输出 ['node', 'python']
console.log(newObj.display == obj.display);     // false