const { createApp } = Vue
createApp({
    data() {
        return {
            client: null,
            accId: null,
            accounts: null,
            cards: [],
            creditCards: [],
            debitCards: [],
            windowWidth: window.innerWidth,
            currentTheme: null,
            newCardColor: null,
            newCardType: null,
            loaded: false,
            cardsToRenew:[],
            clientAccounts:[]
        }
    },
    beforeUpdate() {
      
    },

    updated() {
        //const app = document.getElementById('app')
        $(document).foundation()
       
    },
    unmounted() {
        console.log('Uunmount')
    },

    mounted() {
        console.log('Mounted')
        window.addEventListener('resize', this.onResize)
        //const app = document.getElementById('app');
        $(document).foundation()
    },
    created() {
        this.loadData()
    },

    methods: {
        loadData: function () {
            this.isLoading()
            axios
                .get(`http://localhost:8080/api/clients/current`)
                .then(data => {
                    this.client = data.data;
                    this.cards = this.client.cards
                    this.filterCreditCards()
                    this.filterDebitCards()
                    this.addDateObj(this.cards)     
                    this.sortAccounts()           
                }
                )
                .catch(error => {
                    console.log(error);
                })
                this.finishedLoading()
        },

        filterCreditCards() {
            this.creditCards = this.cards.filter(card => card.cardType === 'CREDIT')
        },

        filterDebitCards() {
            this.debitCards = this.cards.filter(card => card.cardType === 'DEBIT')
        },

        createNewCard() {
            console.log(this.newCardColor)
            console.log(this.newCardType)
            axios.post('/api/clients/current/cards', `type=${this.newCardType}&color=${this.newCardColor}`)
                .then(response =>
                    this.loadData()
                    
                )
                .catch(error => console.log(error))
        },

        addDateObj(arr){
            arr.forEach(card => {
                card.fromDateObj = new Date(card.fromDate)
                card.thruDateObj = new Date(card.thruDate)
            });
        },
        checkExpirationDate(card) {
            let today = new Date();
            let expirationDate = new Date(card.thruDateObj);
            let timeDiff = Math.abs(expirationDate.getTime() - today.getTime());
            let daysDifference = Math.ceil(timeDiff / (1000 * 3600 * 24));    
            return daysDifference <= 7;
        },

        renewCard(card) {
            axios.post('/api/clients/current/renew-card', `cardId=${card.id}`)
            .then(response =>
                console.log(response),
                this.loadData()
                
            )
            .catch(error => console.log(error))
            this.loadData()
        },

        sortAccounts() {
            let sortedAccounts = this.client.accounts.sort((acc1, acc2) => acc1.id - acc2.id)
            this.clientAccounts = sortedAccounts
        },

        isLoading() {
            this.loading = true;
        },
        finishedLoading() {
            this.loading = false;
        },






        toggleTogglerLarge() {
            const toggler = document.getElementById("togglerLarge");
            toggler.classList.toggle('opened'); toggler.setAttribute('aria-expanded', toggler.classList.contains('opened'))
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