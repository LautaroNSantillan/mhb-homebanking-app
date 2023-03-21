const { createApp } = Vue
createApp({
    data() {
        return {
            currentId: null,
          client: null,
          destination:null,
          description:"",
          originAccNumber:null,
          destinationAccNumber:null,
          amount:null,
          filteredAccounts: [],
          invalidTransaction:false,
          transactionError:"",
          transactionSuccess:false,
          
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
       this.loadData()
    },

    methods: {
        loadData() {
            axios
                .get(`http://localhost:8080/api/clients/current`)
                .then(data => {
                    console.log(data);
                    this.client = data.data;
                    console.log(this.client);
                   // this.currentId=this.client=localStorage.getItem('clientId');
                }
                )
                .catch(error => {
                    console.log(error);
                })
        },

        makeTransaction() {
            axios.post('/api/transaction',`originAccNumber=${this.originAccNumber}&destinationAccNumber=${this.destinationAccNumber}&amount=${this.amount}&description=${this.description}`)
            .then(response => {
                if(response.data=='Transaction successful'){
                    console.log(response)
                    this.transactionSuccess=true;
                    setTimeout(() => {
                        this.transactionSuccess = false;
                      }, 5000)
                }
            }
                )

                .catch(error =>  {
                        console.log(error)
                        this.transactionError = error.message
                        console.log(this.transactionError)
                        this.invalidTransaction = true
                        setTimeout(() => {
                            this.invalidTransaction = false;
                          }, 5000)
                    })
        },

        filterAccounts() {
            this.filteredAccounts = this.client.accounts.filter(account => account.number !== this.originAccNumber)
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
			
        },

        logOut(){
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
        },

        toggleToggler() {
            const toggler = document.getElementById("toggler");
            toggler.classList.toggle('opened'); toggler.setAttribute('aria-expanded', toggler.classList.contains('opened'))
        },
        toggleTogglerLarge() {
            const toggler = document.getElementById("togglerLarge");
            toggler.classList.toggle('opened'); toggler.setAttribute('aria-expanded', toggler.classList.contains('opened'))
        },
        toggleTogglerMain() {
            const toggler = document.getElementById("toggler");
            const offCanvas = document.getElementById("offCanvas");
            if (offCanvas.classList.contains('is-open')) {
                toggler.classList.toggle('opened'); toggler.setAttribute('aria-expanded', toggler.classList.contains('opened'))
            }

        },
        
    }
}).mount("#transaction")