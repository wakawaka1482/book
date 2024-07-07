new Vue({
    el: '#app',
    data: {
        dataList: [],
        pagination: {
            currentPage: 1,
            pageSize: 10,
            total: 0,
            queryString: ''
        },
        username: ''  // 存储用户名
    },
    methods: {
        getImageUrl(imageData) {
            if (!imageData) {
                // 如果 imageData 无效，返回一个占位符图片的 URL
                return '../img/img.png';  // 请用实际占位符图片的路径替换
            }
            return imageData.startsWith('data:image/') ? imageData : 'data:image/jpeg;base64,' + imageData;
        },
        findPage() {
            axios.post('/collect/findPageUser', {
                currentPage: this.pagination.currentPage,
                pageSize: this.pagination.pageSize,
                bookname: this.pagination.queryString,
                username: this.username
            }).then(response => {
                this.dataList = response.data.data.records.map(record => {
                    return {
                        ...record,
                        image: record.image  // 直接使用后端返回的 base64 编码字符串
                    };
                });
                this.pagination.total = response.data.data.total;
            }).catch(error => {
                console.error('API 请求错误:', error);
            });
        },
        handleCurrentChange(val) {
            this.pagination.currentPage = val;
            this.findPage();
        },
        handleDelete(row) {
            axios.post('/collect/delete', { collectrecordid: row.collectrecordid })
                .then(response => {
                    if (response.data.flag) {
                        this.$message({
                            type: 'success',
                            message: response.data.message|| '归还成功'
                        });
                        this.findPage();
                    } else {
                        this.$message.error(response.data.message);
                    }
                }).catch(error => {
            });
        },
        getUsername() {
            // 从localStorage获取用户名
            this.username = localStorage.getItem('username');
        }
    },
    mounted() {
        this.getUsername();  // 获取用户名
        this.findPage();  // 获取数据
    },
});