var vue;
vue = new Vue({
    el: '#app',
    data: {
        dataList: [],
        dialogFormVisible: false,
        dialogFormVisible4Edit: false,
        formData: {},
        selectedFile: null,  // 用于存储选中的文件
        rules: {
            type: [{ required: true, message: '图书类别为必填项', trigger: 'blur' }],
            name: [{ required: true, message: '图书名称为必填项', trigger: 'blur' }]
        },
        pagination: {
            currentPage: 1,
            pageSize: 10,
            total: 0,
            name: "",
            type: "",
            description: ""
        },
        username: null,  // 添加用户名
    },
    created() {
        this.getUsernameFromUrl();  // 从URL获取用户名
        if (!this.username) {
            console.error('未找到用户名');
            alert('未找到用户名，请重新登录');  // 提示用户未找到用户名
            window.location.href = 'login.html';  // 重定向到登录页面
        } else {
            localStorage.setItem('username', this.username);
        }
        this.getAll();
    },
    methods: {
        getAll() {
            let param = "?name=" + this.pagination.name;
            param += "&type=" + this.pagination.type;
            param += "&description=" + this.pagination.description;

            axios.get("/books/" + this.pagination.currentPage + "/" + this.pagination.pageSize + param)
                .then((res) => {
                    this.pagination.total = res.data.data.total;
                    this.pagination.currentPage = res.data.data.current;
                    this.pagination.pagesize = res.data.data.size;
                    this.dataList = res.data.data.records;
                }).catch(error => {
                console.error('API 请求错误:', error);
            });
        },
        getUsernameFromUrl() {
            const urlParams = new URLSearchParams(window.location.search);
            this.username = urlParams.get('username');
            if (!this.username) {
                this.username = localStorage.getItem('username');
            }
        },
        handleCreate() {
            this.dialogFormVisible = true;
            this.resetForm();
        },
        resetForm() {
            this.formData = {};
            this.selectedFile = null;  // 重置选中的文件
        },
        resetInput() {
            this.pagination.name = "";
            this.pagination.type = "";
            this.pagination.description = "";
            this.getAll();
        },
        handleFileChange(event) {
            this.selectedFile = event.target.files[0];  // 获取选中的文件
        },
        handleAdd() {
            const formData = new FormData();
            formData.append('book', JSON.stringify(this.formData));
            formData.append('image', this.selectedFile);

            axios.post('/books', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }).then(res => {
                if (res.data.flag) {
                    this.dialogFormVisible = false;
                    this.$message.success(res.data.msg);
                    this.getAll();  // 刷新数据
                } else {
                    this.$message.error(res.data.msg);
                }
            }).catch(error => {
                this.$message.error('添加图书失败');
                console.error(error);
            });
        },
        cancel() {
            this.dialogFormVisible = false;
            this.dialogFormVisible4Edit = false;
            this.$message.info("操作取消");
        },
        getEastEightZoneTime() {
            const currentDate = new Date();
            const offset = currentDate.getTimezoneOffset();
            const cstDate = new Date(currentDate.getTime() + (offset + 480) * 60000);

            // 格式化为字符串
            const year = cstDate.getFullYear();
            const month = String(cstDate.getMonth() + 1).padStart(2, '0');
            const day = String(cstDate.getDate()).padStart(2, '0');
            const hours = String(cstDate.getHours()).padStart(2, '0');
            const minutes = String(cstDate.getMinutes()).padStart(2, '0');
            const seconds = String(cstDate.getSeconds()).padStart(2, '0');
            return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
        },
        handleLend(row) {
            this.getUserId().then(userId => {
                if (row.number <= 0) {
                    this.$message.error('借阅图书失败：书籍数量为0');
                    return;
                }
                const lendtime = this.getEastEightZoneTime();
                axios.post("/lend/add", {
                    bookid: row.id,
                    userid: userId,
                    lendtime: lendtime
                }).then((res) => {
                    if (res.data.flag) {
                        this.$message({
                            type: 'success',
                            message: res.data.message || '借阅成功'
                        });
                    } else {
                        this.$message({
                            type: 'error',
                            message: res.data.message || '借阅图书失败'
                        });
                    }
                }).catch(error => {
                    this.$message.error('借阅图书失败');
                });
            }).catch(error => {
                this.$message.error('获取用户ID失败');
            });
        },
        handleCollect(row) {
            this.getUserId().then(userId => {
                axios.get("/books/search", {
                    params: {
                        name: row.name,
                        type: row.type,
                        description: row.description
                    }
                }).then((res) => {
                    if (res.data.flag && res.data.data.length > 0) {
                        const bookId = res.data.data[0].id;
                        if (userId && bookId) {
                            const lenddate = this.getEastEightZoneTime();

                            axios.post("/collect/add", {
                                bookid: bookId,
                                userid: userId,
                                lenddate: lenddate
                            }).then((res) => {
                                if (res.data.flag) {
                                    this.$message({
                                        type: 'success',
                                        message: res.data.message || '收藏成功'
                                    });
                                } else {
                                    this.$message.error(res.data.message || '收藏图书失败');
                                }
                            }).catch(error => {
                                this.$message.error('收藏图书失败');
                            });
                        } else {
                            this.$message.error('无效的用户ID或图书ID');
                        }
                    } else {
                        this.$message.error('未找到匹配的图书');
                    }
                }).catch(error => {
                    this.$message.error('搜索图书失败');
                });
            }).catch(error => {
                this.$message.error('获取用户ID失败');
            });
        },
        handleCurrentChange(currentPage) {
            this.pagination.currentPage = currentPage;
            this.getAll();
        },
        getImageUrl(imageData) {
            return 'data:image/jpeg;base64,' + imageData;
        },
        getUserId() {
            return new Promise((resolve, reject) => {
                if (!this.username) {
                    reject('未找到用户名');
                    return;
                }

                const encodedUsername = encodeURIComponent(this.username);
                const url = `/auth/getUserId?username=${encodedUsername}`;

                axios.get(url)
                    .then(res => {
                        if (res.status === 200 && res.data.flag) {
                            resolve(res.data.data);
                        } else {
                            reject(res.data.msg || '未知错误');
                        }
                    })
                    .catch(error => {
                        if (error.response) {
                            reject(`请求失败，状态码: ${error.response.status}`);
                        } else if (error.request) {
                            reject('请求失败，服务器无响应');
                        } else {
                            reject(`请求失败: ${error.message}`);
                        }
                    });
            });
        }
    }
});
