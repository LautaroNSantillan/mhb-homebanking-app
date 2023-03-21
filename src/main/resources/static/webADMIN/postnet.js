const { createApp } = Vue
createApp({
    data() {
        return {
       digits:null,
       cvv:null,
       amount:null,
       description:null,
       cardType:null,

        }
    },
    mounted() {

    },
    created() {

    },

    methods: {
        
        postPay() {
            let inputString = this.digits; // Example input string
            let formattedString = inputString.replace(/[\s-]+/g, '') // Remove spaces and hyphens
                                 .replace(/(\d{4})(?=\d)/g, '$1-'); // Add dashes every 4 digits
            let payment = {
                number:formattedString,
                cvv:this.cvv,
                amount:this.amount,
                description:this.description,
                cardType:this.cardType.toUpperCase()
            }
            axios.post('/api/clients/current/pay-with-card', payment)
            .then(res=>console.log(res))
        },


    }

}).mount("#app")