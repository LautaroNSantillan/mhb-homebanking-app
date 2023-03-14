const { createApp } = Vue
createApp({
    data() {
        return {
           email:null,
           pwd:null,
           fname:null,
           lname:null,
           regemail:null,
           regpwd:null, 
           filledFname:true,
           filledLname:true,
           filledEmail:true,
           filledPwd:true,
           validForm:false,
           invalidLogin:false,
           loginError:null,
        }
    },
    updated() {
        $(document).foundation();
    },
    beforeUnmount(){
    },
    unmounted() {
    },
    created() {
       // this.login2()
    },

    methods: {
        login() {
            axios.post('/api/login',`email=${this.email}&password=${this.pwd}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => 
                window.location.href='/web/accounts.html'
                )

                .catch(error => {
                    console.log(error)
                    this.loginError = error.message
                    console.log(this.loginError)
                    this.invalidLogin = true
                    setTimeout(() => {
                        this.invalidLogin = false;
                      }, 8000)
                })
        },

        register(){
            let newClient={firstName:this.fname, lastName:this.lname, email:this.regemail, password:this.regpwd}
            axios.post('/api/clients',newClient)
            .then(response =>{
                if(response.status === 201){
                    this.email=this.regemail,
                    this.pwd=this.regpwd,
                    console.log(response.data)
                    console.log(this.email, this.pwd)
                    this.login()
                }
            })
            .catch(error => console.log(error))       
        },

        logOut(){
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
        },
        
        change(){
            console.log(this.email)
            console.log(this.pwd)
            
        },
        validateForm() {
			var fname = document.forms["register"]["firstname"].value;
            var lname = document.forms["register"]["lastname"].value;
            var email = document.forms["register"]["email"].value;
            var pwd = document.forms["register"]["password"].value;
			var regex = /^[a-zA-Z]+$/;
            var emailregex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
			if (fname == "" || !fname.match(regex)) {
				this.filledFname = false;
			}
            else{
                this.filledFname = true;
            }
            if (lname == "" || !lname.match(regex)) {
				this.filledLname = false;
			}
            else{
                this.filledLname = true;
            }
            if (email == "" || !email.match(emailregex)) {
				this.filledEmail = false;
			}
            else{
                this.filledEmail = true;
            }
            if (pwd== "" || pwd.length<3) {
				this.filledPwd = false;
			}
            else{
                this.filledPwd = true;
            }
            if(this.filledFname == true&&this.filledLname == true&&this.filledEmail == true&&this.filledPwd == true){
                this.validForm = true;
            }
			
        }
        
    }
}).mount("#login")