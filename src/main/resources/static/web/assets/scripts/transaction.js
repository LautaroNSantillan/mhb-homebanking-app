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
          clientAccounts:[]
          
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
            this.isLoading()
            axios
                .get(`/api/clients/current`)
                .then(data => {
                    console.log(data);
                    this.client = data.data;
                    console.log(this.client);
                   this.sortAccounts()
                }
                )
                .catch(error => {
                    console.log(error);
                })
                this.finishedLoading()
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
              })
              .catch(error =>  {
                console.log(error)
                this.transactionError = error.response.data.message
                console.log(this.transactionError)
                this.invalidTransaction = true
                setTimeout(() => {
                  this.invalidTransaction = false;
                }, 5000)
              })
          
            this.destination=null;
            this.description="";
            this.originAccNumber=null;
            this.destinationAccNumber=null;
            this.amount=null;
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

        isLoading() {
            this.loading = true;
        },
        finishedLoading() {
            this.loading = false;
        },


        sortAccounts() {
            let sortedAccounts = this.client.accounts.sort((acc1, acc2) => acc1.id - acc2.id)
            this.clientAccounts = sortedAccounts
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
        logOut() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
                .then(response => {
                    window.location.href = '/web/index.html'
                }
                )
        },
        
    }
}).mount("#transaction")