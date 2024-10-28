const togglesidebar=()=>{

    if( $('.sidebar').is(":visible") ){

        $(".sidebar").css("display","none");
        $(".content").css("margin-left","0");
        $(".heading").css("margin-left","0");
        $(".parts").css("margin-left","0");
    }
    else{
        $(".sidebar").css("display","block");
        $(".content").css("margin-left","20%");
        $(".pimg").css("margin-left","7%");
    }
};

//REQUEST TO SERVER TO CREATE ORDER
const paymentstart=(totalamt)=>{

    //below are the predefined values
    $.ajax(
        {   url:'/user/create_order',
            data:JSON.stringify({amount:totalamt,info:'order_request'}),
            contentType:'application/json',
            type:"POST",
            dataType:'json',

            //invoke when success
            success:function(response)
            {
                console.log(response);
                if(response.status=='created'){
                    //open payment form
                    var options = {
                        "key": "rzp_test_epfZlkjtEtSgVY", // Enter the Key ID generated from the Dashboard
                        "amount": response.amount, // Amount is in currency subunits. Default currency is INR. Hence, 50000 refers to 50000 paise
                        "currency": "INR",
                        "name": "YAMAHA MOTORS",
                        "description": "Test Transaction",
                        "image": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRStV3pjSXZHqcgAZKY7Vad23c6SPNc4cjr7Q&s",
                        "order_id": response.id, //This is a sample Order ID. Pass the `id` obtained in the response of Step 1
                        "handler": function(response) {
                            console.log(response.razorpay_payment_id);
                            console.log(response.razorpay_order_id);
                            console.log(response.razorpay_signature);
                            Swal.fire({
                                position: "center",
                                icon: "success",
                                title: "Payment Succesfull",
                                showConfirmButton: false,
                                timer: 1500
                            }).then(() => {
                            // Redirect to the orders page after payment success
                            window.location.href = "/user/orders";
                        });;
                        },
                        "prefill": {
                            "name": "",
                            "email": "",
                            "contact": ""
                        },
                        "notes": {
                            "address": "Razorpay Corporate Office"
                        },
                        "theme": {
                            "color": "#3399cc"
                        },
                    }; 

                    var rzp1 = new Razorpay(options);
				    rzp1.on('payment.failed', function(response) {
					console.log(response.error.code);
					console.log(response.error.description);
					console.log(response.error.source);
					console.log(response.error.step);
					console.log(response.error.reason);
					console.log(response.error.metadata.order_id);
					console.log(response.error.metadata.payment_id);
					Swal.fire({
						icon: "error",
						title: "Oops...",
						text: "Something went wrong!",

					 });
				    });

			     	rzp1.open();

		     	}
		    },

                //invoke when error
            error:function(error){ 
                    console.log(error); 
                    alert('something went wrong');
            }
        }
    )
 };

 const search=()=>{
    let query=$("#searchinput").val();

    if(query==''){
         $(".search-result").hide();
    }
    else{
        //search
        let url=`http://localhost:8080/search/${query}`;
        fetch(url).then((response)=>{
            return response.json();    
        }).then((data)=>{
            console.log(data);

            let text="<div class='list-group'>";
            data.forEach(element => {
                text+=`<a href='/user/singleproduct/ ${element.productid}' class='list-group-item list-group-action'> ${element.productName} </a> `;
            });

            text+='</div>'


            $(".search-result").html(text);
            $(".search-result").show();
        })

    }
    
 }