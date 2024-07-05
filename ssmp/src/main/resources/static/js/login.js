// axios全局配置
axios.defaults.headers['token'] = window.localStorage.getItem("token");

var vue = new Vue({
    el: '#app',
    data() {
        return {
            vedioCanPlay: false,
            fixStyle: '',
            form: {role: 1},
            rules: {
                username: [
                    {required: true, message: '请输入用户名', trigger: 'blur'},
                ],
                password: [
                    {required: true, message: '请输入密码', trigger: 'blur'},
                ],
            },
        }
    },
    mounted() {
        localStorage.removeItem("username");
        window.onresize = this.handleResize;
        window.onresize();

        $('#mpanel3').codeVerify({
            type: 2,
            figure: 10,
            arith: 0,
            width: '200px',
            height: '40px',
            fontSize: '30px',
            btnId: 'check-btn2',
            ready: function() {},
            success: this.verifySuccess,
            error: this.verifyError
        });
    },
    methods: {
        handleResize() {
            const windowWidth = document.body.clientWidth;
            const windowHeight = document.body.clientHeight;
            const windowAspectRatio = windowHeight / windowWidth;
            let videoWidth;
            let videoHeight;
            if (windowAspectRatio < 0.5625) {
                videoWidth = windowWidth;
                videoHeight = videoWidth * 0.5625;
                this.fixStyle = {
                    height: windowWidth * 0.5625 + 'px',
                    width: windowWidth + 'px',
                    'margin-bottom': (windowHeight - videoHeight) / 2 + 'px',
                    'margin-left': 'initial'
                };
            } else {
                videoHeight = windowHeight;
                videoWidth = videoHeight / 0.5625;
                this.fixStyle = {
                    height: windowHeight + 'px',
                    width: windowHeight / 0.5625 + 'px',
                    'margin-left': (windowWidth - videoWidth) / 2 + 'px',
                    'margin-bottom': 'initial'
                };
            }
        },
        canplay() {
            this.vedioCanPlay = true;
        },
        createValidCode(data) {
            this.validCode = data;
        },
        verifySuccess() {
            localStorage.removeItem("dss");
        },
        verifyError() {
            localStorage.setItem("dss", "dss");
            alert('验证码不匹配！请重新填写!!!');
            window.location.href = "login.html";
        },
        login() {
            const isVerified = localStorage.getItem("dss") !== "dss";
            if (!isVerified) {
                alert('请完成验证码验证');
                return;
            }

            this.$refs['form'].validate((valid) => {
                if (valid) {
                    localStorage.setItem("username", this.form.username);
                    axios.post("/auth/login", this.form).then(res => {
                        if (res.data.flag) {
                            alert('登录成功');
                            window.location.href = res.data.redirectUrl;
                        } else {
                            alert(res.data.msg || '账号或密码错误');
                        }
                    }).catch(error => {
                        alert('账号或密码错误');
                        console.error('Error:', error);
                        window.location.href = "login.html";
                    });
                }
            });
        },
        judgeURL() {
            window.location.href = "./register.html";
        },
    }
});
