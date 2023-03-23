const { createApp } = Vue
createApp({
    data() {
        return {
            client: null,
            newCardColor: null,
            newCardType: null,
            cardsToRenew: [],
            createdCard: null,
            cardReqFailed: false,
            missingData: false,
            errMsg: "",
            clientAccounts: [], 
        }
    },
    beforeUpdate() {

    },

    updated() {
        $(document).foundation()

    },
    unmounted() {

    },

    mounted() {
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
                    this.client = data.data
                    console.log(data)
                    this.sortAccounts()
                }
                )
                .catch(error => {
                    console.log(error);
                })
                this.finishedLoading()
        },

        createNewCard() {
            console.log(this.newCardColor)
            console.log(this.newCardType)
            axios.post('/api/clients/current/cards', `type=${this.newCardType}&color=${this.newCardColor}`)
                .then(response => {
                    if (response.status == "201") {
                            this.createdCard = response.data
                    }
                }
                )
                .catch(error => {
                    if (error.response.status == 500) {
                        console.log(error)
                        this.errMsg = error.response.data,
                            this.cardReqFailed = true,
                            setTimeout(() => {
                                this.cardReqFailed = false;
                            }, 6000)
                    }
                   else if (error.response.status == 400) {
                        console.log(error)
                        this.errMsg = error.response.data.message,
                            this.missingData = true,
                            setTimeout(() => {
                                this.missingData = false;
                            }, 6000)
                    }
                }
                )
        },

        renewCard(card) {
            axios.post('/api/clients/current/renew-card', `cardId=${card.id}`)
                .then(response =>
                    this.loadData()
                )
                .catch(error => console.log(error))
        },


        sortAccounts() {
            let sortedAccounts = this.client.accounts.sort((acc1, acc2) => acc1.id - acc2.id)
            this.clientAccounts = sortedAccounts
        },

        toggleTogglerLarge() {
            const toggler = document.getElementById("togglerLarge");
            toggler.classList.toggle('opened'); toggler.setAttribute('aria-expanded', toggler.classList.contains('opened'))
        },

        isLoading() {
            this.loading = true;
        },
        finishedLoading() {
            this.loading = false;
        },

        //------

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