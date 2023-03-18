const { createApp } = Vue
createApp({
    data() {
        return {
            fName: "",
            lName: "",
            email: "",
            fNameMod: "",
            lNameMod: "",
            emailMod: "",
            clientsData: null,
            clients: null,
            clientIdMod: null,
        }
    },
    mounted() {

    },
    created() {
        this.loadData()
    },

    methods: {
        
        loadData: function () {
            axios
                .get('http://localhost:8080/rest/clients')
                .then(data => {
                    this.clientsData = data;
                    this.clients = data.data._embedded.clients;
                }
                )
                .catch(error => {
                    console.log(error);
                })
        },
        
        postClient: function (client) {
            axios.post('http://localhost:8080/rest/clients', client).then(res=>this.loadData())
        },

        addClient: function () {
            let nClient={
                firstName: this.fName,
                lastName: this.lName,
                userEmail: this.email
            }
            if(this.fName && this.lName && this.email){
              this.postClient(nClient)
              this.fName=""
              this.lName=""
              this.email=""
            }
        },

        removeClient: function (client) {
            axios.delete(client._links.self.href).then(res=>this.loadData())
        },

        getClientId: function(client){
            this.clientIdMod= client._links.self.href
            console.log(this.clientIdMod)
        },

        modClient: function(){
           axios.patch(this.clientIdMod, this.modClientAux() ).then(res=>this.loadData())
        },

        modClientAux: function(){
            let modClient={}
            if (this.fNameMod) modClient.firstName=this.fNameMod
            if (this.lNameMod) modClient.lastName=this.lNameMod
            if (this.emailMod) modClient.userEmail=this.emailMod
            console.log(modClient)
            return modClient
        }

    }

}).mount("#app")