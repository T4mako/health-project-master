<template>
  <div>
    <el-table ref="singleTable" :data="tableData"  highlight-current-row @current-change="handleCurrentChange"
      style="width: 100%" size="medium" :lazy="true" :header-cell-style="{background:'rgb(0,0,0,0)',color:'#fff'}">
      <el-table-column type="index" width="50">
      </el-table-column>
      <el-table-column property="date" label="日期" width="120">
      </el-table-column>
      <el-table-column property="name" label="姓名" width="120">
      </el-table-column>
      <el-table-column property="address" label="地址">
      </el-table-column>
    </el-table>
  </div>

</template>

<script>
export default {
  data() {
    return {
      tableData: [{
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-04',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1517 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1519 弄'
      }, {
        date: '2016-05-03',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1516 弄'
      }],
      currentRow: null
    }
  },

  methods: {
    setCurrent(row) {
      this.$refs.singleTable.setCurrentRow(row);
    },
    handleCurrentChange(val) {
      this.currentRow = val;
    }
  }
}
</script>

<style>
/*最外层透明*/
/*最外层透明*/
.el-table, .el-table__expanded-cell{
  color: #ffffff;
    background-color: transparent;
}
/* 表格内背景颜色 */
.el-table th,
.el-table tr,
.el-table td {
    background-color: transparent;
}

/* 表格表头字体颜色 */
.el-table thead {
  color: #ffffff;
  font-weight: 500;
}

.el-table--enable-row-hover .el-table__body tr:hover > td {
  background-color: rgba(174, 209, 249, 0.25) !important;
}

.el-table__body tr.current-row > td {
  background-color: rgba(48, 141, 247, 0.75) !important;
}

</style>