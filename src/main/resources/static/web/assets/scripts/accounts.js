const { createApp } = Vue
createApp({
    data() {
        return {
            eyeToggled: true,
            client: undefined,
            clientAccounts: [],
            currentId: null,
            totalBalance: 0,
            createdAcc: false,
            maxAcc: false,
            requestedAmount:0,
            requestedPayments:0,
            loanDTO:undefined,
            finalPayments:0,
            loanId:"",
            loanType:"",
            destinationAccNumber:null,
            loanSuccess:false,
            loanError:false,
            loading:true

        }
    },
    updated() {
        $(document).foundation();
    },
    beforeUnmount() {
    },
    unmounted() {
    },
    created() {
        // this.currentId = new URLSearchParams(location.search).get("clientId");
        // console.log(this.currentId);
        this.loadData()
        // this.getCurrentTheme()
        // this.setCurrentTheme()
    },

    methods: {
        loadData() {
            this.isLoading()
            axios
                .get(`http://localhost:8080/api/clients/current`)
                .then(data => {
                    console.log(data);
                    this.client = data.data;
                    console.log(this.client);
                    this.getTotalBalance()
                    this.sortAccounts()
                    localStorage.setItem('clientId', this.client.id);
                    this.finishedLoading()
                }
                )
                .catch(error => {
                    console.log(error);
                })
        },
        getTotalBalance() {
            this.totalBalance = this.client.accounts.reduce((acc, account) => acc + account.balance, 0)
        },

        sortAccounts() {
            let sortedAccounts = this.client.accounts.sort((acc1, acc2) => acc1.id - acc2.id)
            this.clientAccounts = sortedAccounts
        },
        logOut() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
                .then(response => {
                    window.location.href = '/web/index.html'
                }
                )
        },

        createNewAcc() {
            axios.post('/api/clients/current/accounts')
                .then(response => {
                    if (response.status == "201") {
                        console.log(response),
                        this.createdAcc = true,
                            this.loadData(),
                            setTimeout(() => {
                                this.createdAcc = false;
                            }, 6000)
                    }

                }
                )
                .catch(error => {
                    console.log(error);      
                    if(error.code == "ERR_BAD_REQUEST"){
                        console.log(error),
                    this.maxAcc = true,
                    setTimeout(() => {
                        this.maxAcc = false;
                    }, 6000)
                    }
                })    
        },

        requestLoan(){
            let newLoan={
            loanId:this.loanId,
            amount:this.requestedAmount,
            payments:this.requestedPayments,
            destinationAccNumber:this.destinationAccNumber}
            axios.post('/api/loans',newLoan)
            .then(response =>{
                console.log(response)
                if(response.status === 201){
                    this.loanSuccess=true,
                    setTimeout(() => {
                        this.loanSucces=false;
                      }, 5000)
                }
            })
            .catch(error =>
                this.loanError=true,
                setTimeout(() => {
                    this.loanError=false;
                  }, 5000))      
        },

        getPayments(){
            axios.post('/api/loans/final-payments', `amount=${this.requestedAmount}&payments=${this.requestedPayments}`)
            .then(r=>{
                console.log(r)
                this.finalPayments = r.data;
            })
        },
        getLoanDTO(){
            console.log(this.loanId)
            axios.get(`/api/loans/${this.loanId}/DTO`)
            .then(r=>{
                console.log(r)
                this.loanDTO = r.data;
            })
        },

        isLoading(){
            this.loading = true;
        },
        finishedLoading(){
            this.loading = false;
        },

        //--------------TOGGLERS
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
        eyeToggler() {
            const eyeToggler = document.getElementById("eyeToggler");
            this.eyeToggled = !this.eyeToggled;
            eyeToggler.classList.toggle('fa-eye-slash')
            eyeToggler.classList.toggle('fa-eye')
        },
        accountFocused() {
            const accountCards = document.getElementsByClassName('cardContainer');

            Array.from(accountCards).forEach(card => {
                const eye = card.querySelector('.accEye');
                if (document.activeElement === card) {
                    eye.classList.toggle('fa-eye-slash')
                    eye.classList.toggle('fa-eye')
                }
                card.onfocusout = () => {
                    eye.classList.toggle('fa-eye-slash')
                    eye.classList.toggle('fa-eye')
                }
            });
        },

        // changeTheme(color) {
        //     const body = document.querySelector('body');
        //     body.className = body.className = ('');
        //     body.classList.add(color + "-theme");
        //     this.setCurrentTheme()
        // },
        // setCurrentTheme() {
        //     const body = document.querySelector('body');
        //     localStorage.setItem('theme', body.className);
        //     console.log(localStorage.getItem('theme'))
        // },
        // getCurrentTheme() {
        //     const body = document.querySelector('body');
        //     if (localStorage.getItem('theme')) {
        //         body.className = body.className = ('');
        //         body.classList.add(localStorage.getItem('theme'));
        //     }}
    }
}).mount("#app")