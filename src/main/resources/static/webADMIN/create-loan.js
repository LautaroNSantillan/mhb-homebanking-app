const { createApp } = Vue
createApp({
    data() {
        return {
          id:null,
          maxAmount:null,
          interest:null,
          name:null,
          payments:[],
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
        createLoan(){
            let paymentsArr = this.payments.split(/[ ,]+/);
            let newLoan={
                id:this.id,
                name:this.name,
                maxAmount:this.maxAmount,
                interestRate:this.interest,
                payments:paymentsArr,
                isPredefinedLoan : false
            }
            axios.post('/api/loans/create',newLoan,{headers: {
                'Content-Type': 'application/json'
              }})
            .then(response =>{
                console.log(response.data)
            })
            .catch(error => console.log(error))       
        },

        
    }
}).mount("#app")