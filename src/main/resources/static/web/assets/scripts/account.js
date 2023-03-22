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
            fromDate: null,
            toDate: null,
            slicedTransactions: [],
            currentPage: 1,
            itemsPerPage: 5,
            totalPages: 1,
            paginationDiv: undefined,
            loading:true,
            clientAccounts:[],
        }
    },

    updated() {
        this.paginationDiv = document.getElementById('paginationDiv')
        console.log('updated', this.paginationDiv)
        this.renderNumbers()
        console.log(this.paginationDiv)
        document.addEventListener('click', this.onDocumentClick)
        $(document).foundation();
        this.datePicker()
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
        window.addEventListener('click', this.onDocumentClick)
        $(document).foundation();
        //  this.accId=this.client=localStorage.getItem('clientId');
        this.accId = new URLSearchParams(location.search).get("accountId");
        this.loadClient()
        this.loadData()
        this.getCurrentTheme()
        this.setCurrentTheme()
        this.getClient()
        this.datePicker()
        this.paginationDiv = document.getElementById('paginationDiv')
        console.log(this.paginationDiv)
    },

    methods: {
        loadData: function () {
            this.isLoading()
            axios
                .get(`http://localhost:8080/api/accounts/${this.accId}`)
                .then(data => {
                    this.accounts = data.data;
                    console.log(this.accounts)
                    console.log(this.accounts.transactions)
                    // this.accounts.transactions.sort((a, b) =>b.id -a.id)      funciona si las creas en orden
                    this.sortTransactions()
                    this.renderRows()
                    this.setPageNumber();
                    this.renderNumbers()
                }

                )
                .catch(error => {
                    console.log(error);
                })
                this.finishedLoading()
        },

        isLoading() {
            this.loading = true;
        },
        finishedLoading() {
            this.loading = false;
        },

        loadClient() {
            this.isLoading()
            axios
                .get(`http://localhost:8080/api/clients/current`)
                .then(data => {
                    console.log(data);
                    this.client = data.data;
                    this.sortAccounts()
                }
                )
                .catch(error => {
                    console.log(error);
                })
                this.finishedLoading()
        },
        sortAccounts() {
            let sortedAccounts = this.client.accounts.sort((acc1, acc2) => acc1.id - acc2.id)
            this.clientAccounts = sortedAccounts
        },

        sortTransactions() {
            const transactions = [...this.accounts.transactions]
            const newTransactions = transactions.map(transaction => {
                const newTransaction = { ...transaction };
                newTransaction.date = new Date(transaction.date);
                newTransaction.dateObj = new Date(transaction.date);
                console.log(newTransaction.dateObj)
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

        getClient() {
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

        filterTransactionsByDate() {
            let fromDate = new Date(this.fromDate);
            fromDate.setHours(0, 0, 0, 0);
            let toDate = new Date(this.toDate);
            toDate.setHours(23, 59, 59, 999);

            this.sortedTransactions = this.sortedTransactions.filter((transaction) => {
                let transactionDate = new Date(transaction.date);
                return transactionDate >= fromDate && transactionDate <= toDate;
            });
        },

        downloadPDF() {
            const data = {
                fromDate: this.fromDate.toISOString(),
                toDate: this.toDate.toISOString(),
                accountNumber: this.accounts.number
            };
            axios.post('/api/export-to-PDF-stream-output', data, { responseType: 'blob' })
                .then(response => {
                    console.log(response)
                    const url = window.URL.createObjectURL(response.data);
                    const link = document.createElement('a');
                    link.href = url;
                    link.setAttribute('download', response.headers['content-disposition'].split('filename=')[1]);
                    document.body.appendChild(link);
                    link.click();
                })
                .catch(error => {
                    console.log(error);
                });
        },

        getrans() {
            const data = {
                fromDate: this.fromDate.toISOString(), // convert fromDate to ISO string
                toDate: this.toDate.toISOString(), // convert toDate to ISO string
                accountNumber: this.accounts.number
            };
            axios.post('/api/getrans', data)
                .then(response => {
                    console.log(response)
                })
                .catch(error => {
                    console.log(error);
                });
        },

        downloadPDF2() {
            const data = {
                fromDate: this.fromDate.toISOString(), // convert fromDate to ISO string
                toDate: this.toDate.toISOString(), // convert toDate to ISO string
                accountNumber: this.accounts.number
            };
            axios.post('/api/transaction/export-to-pdf', data, { responseType: 'blob' })
                .then(response => {
                    // const url = window.URL.createObjectURL(new Blob([response.data]));
                    // const link = document.createElement('a');
                    // link.href = url;
                    // link.setAttribute('download', `transactions-${this.accounts.number}.pdf`);
                    // document.body.appendChild(link);
                    // link.click();
                })
                .catch(error => {
                    console.log(error);
                });
        },

        datePicker() {
            // implementation of custom elements instead of inputs
            var startDate = new Date();
            startDate.setHours(0, 0, 0, 0);
            this.fromDate = startDate
            var endDate = new Date();
            endDate.setHours(0, 0, 0, 0);
            this.toDate = endDate
            $('#dp4').fdatepicker()
                .on('changeDate', function (ev) {
                    if (ev.date.valueOf() > endDate.valueOf()) {
                        $('#alert').show().find('strong').text('The start date can not be greater then the end date');
                    } else {
                        $('#alert').hide();
                        startDate = new Date(ev.date);
                        this.fromDate = startDate
                        console.log(startDate)
                        $('#startDate').text($('#dp4').data('date'));
                    }
                    $('#dp4').fdatepicker('hide');
                });
            $('#dp5').fdatepicker()
                .on('changeDate', function (ev) {
                    if (ev.date.valueOf() < startDate.valueOf()) {
                        $('#alert').show().find('strong').text('The end date can not be less then the start date');
                    } else {
                        $('#alert').hide();
                        endDate = new Date(ev.date);
                        this.toDate = endDate
                        console.log(this.toDate)
                        $('#endDate').text($('#dp5').data('date'));
                    }
                    $('#dp5').fdatepicker('hide');
                });
        },


        //----------------------------------------------PAGINATOR--------------------------------
        renderRows() {
            if(this.sortedTransactions.length>0){
               this.slicedTransactions = this.sortedTransactions.slice(this.itemsPerPage * (this.currentPage - 1),
                this.itemsPerPage * this.currentPage); 
            }
            
        },
        setPageNumber() {
            let numOfTransactions = this.sortedTransactions.length;
            this.totalPages = Math.ceil(numOfTransactions / this.itemsPerPage);
            console.log(this.totalPages);
        },
        renderNumbers() {
            if(this.sortedTransactions.length>0){

            
            let paginationDiv = document.getElementById('paginationDiv')
            console.log(paginationDiv)
            console.log(this.totalPages > 0)

            paginationDiv.innerHTML = `  <li ><a href="#" data-pagination="firstPage"><i class="fa-solid fa-backward-step"></i> First page </a></li>`

            if (this.currentPage != 1) {

                paginationDiv.innerHTML +=
                    ` 
              <li ><a href="#" data-pagination="previous"> <i class="fa-solid fa-chevron-left"></i>  <span > Previous </span></a></li>
              
              `
            }

            if (this.totalPages > 0) {
                for (let i = 1; i <= this.totalPages; i++) {
                    paginationDiv.innerHTML +=
                        ` 
                 <li> <a href="#" data-pagination="pageNumber"> ${i}</a> </li>
                
                 `
                }
            }

            if (this.currentPage !== this.totalPages) {

                paginationDiv.innerHTML +=
                    ` 
            <li ><a href="#" data-pagination="next">  Next <i class="fa-solid fa-chevron-right"></i> </a></li>
            
            `
            }
            paginationDiv.innerHTML += ` <li ><a href="#" data-pagination="lastPage"><i class="fa-solid fa-forward-step"></i>  <span >Last page</span> </a></li>`
        }

        },
        onDocumentClick(event) {
            console.log(event.target.dataset.pagination)
            switch (event.target.dataset.pagination) {
                case "pageNumber":
                    this.currentPage = Number(event.target.innerText);
                    break;
                case "previous":
                    this.currentPage = this.currentPage == 1 ? this.currentPage :
                        this.currentPage - 1;
                    break;
                case "next":
                    this.currentPage = this.currentPage == this.totalPages ? this.currentPage :
                        this.currentPage + 1;
                    break;
                case "firstPage":
                    this.currentPage = 1;
                    break;
                case "lastPage":
                    this.currentPage = this.totalPages;
                    break;
                default:
            }
            this.renderRows()
            this.renderNumbers()
            console.log(this.currentPage)
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