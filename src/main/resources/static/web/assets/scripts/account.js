const { createApp } = Vue
createApp({
    data() {
        return {
            client: null,
            accId: null,
            accounts: null,
            sortedTransactions: [],
            windowWidth: window.innerWidth,
            currentTheme: null,
        }
    },

    updated() {
        const app = document.getElementById('app');
        $(app).foundation();
    },
    unmounted() {
        console.log('Uunmount')
    },

    mounted() {
        window.addEventListener('resize', this.onResize)
        const app = document.getElementById('app');
        $(app).foundation();
        console.log($(app).foundation())
    },
    created() {
      //  this.accId=this.client=localStorage.getItem('clientId');
        this.accId = new URLSearchParams(location.search).get("accountId");
        this.loadData()
        this.getCurrentTheme()
        this.setCurrentTheme()
        this.getClient()
    },

    methods: {
        loadData: function () {
            axios
                .get(`http://localhost:8080/api/accounts/${this.accId}`)
                .then(data => {
                    this.accounts = data.data;
                    console.log(this.accounts)
                    console.log(this.accounts.transactions)
                   // this.accounts.transactions.sort((a, b) =>b.id -a.id)      funciona si las creas en orden
                    this.sortTransactions()
                }
                
                )
                .catch(error => {
                    console.log(error);
                })
        },

        sortTransactions(){
            const transactions = [...this.accounts.transactions]
            const newTransactions = transactions.map(transaction => {
                const newTransaction = {...transaction};
                newTransaction.date = new Date(transaction.date);
                return newTransaction;
              });
              
              newTransactions.sort((a, b) => b.date - a.date);
              const sortedTransactionsString = JSON.stringify(newTransactions, (key, value) => {
                if (key === 'date') {
                  return value.toLocaleString();
                }
                return value;
              });

              const sortedTransactionsArray = JSON.parse(sortedTransactionsString)

              this.sortedTransactions = sortedTransactionsArray;
              console.log(this.sortedTransactions);
        },

        getClient(){
           axios
            .get(`http://localhost:8080/api/clients/current`)
             .then(data => {
                   this.client = data.data
                     console.log(this.client)
                    
               })
              .catch(error => {
                     console.log(error);
             })
        },
        toggleTogglerLarge() {
            const toggler = document.getElementById("togglerLarge");
            toggler.classList.toggle('opened'); toggler.setAttribute('aria-expanded', toggler.classList.contains('opened'))
        },



        

        onResize(event) {
            this.windowWidth = screen.width
        },

        //--------------TOGGLERS
        toggleToggler() {
            const toggler = document.getElementById("toggler");
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
        changeTheme(color) {
            const body = document.querySelector('body');
            body.className = body.className = ('');
            body.classList.add(color + "-theme");
            console.log(body.className)
        },
        getCurrentTheme() {
            const body = document.querySelector('body');
            if (localStorage.getItem('theme')) {
                body.className = body.className = ('');
                body.classList.add(localStorage.getItem('theme'));
                this.setCurrentTheme()
            }
        },
        setCurrentTheme() {
            const body = document.querySelector('body');
            if (localStorage.getItem('theme')) {
            localStorage.setItem('theme', body.className);
            console.log(localStorage.getItem('theme'))
        }
    }
    }

}).mount("#app")