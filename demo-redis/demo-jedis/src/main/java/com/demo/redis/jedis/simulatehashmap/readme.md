使用数组来模拟HashMap
HashMap本来是二维结构，但是如果内部元素较少，使用二维数组反而浪费空间，还不如
使用一维数组进行存储，需要查找时，因为元素少遍历也很快，甚至比HashMap本身的查找
还要快