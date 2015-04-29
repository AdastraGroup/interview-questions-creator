var Question = React.createClass({

getInitialState: function() {
	return {question: this.props.question, text: this.props.question.text, privateText: this.props.question.privateText, questionType: this.props.question.questionType};
},

update: function(key, value) {

    var data = kv(key, value);

	$.ajax({ url: "/api/questions/" + this.props.question.id, data: JSON.stringify(data), dataType: 'json', type: 'PATCH', headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
		     success: function(result)           { this.onUpdateSuccess(key, value); }.bind(this),
		     error:   function(xhr, status, err) { this.onUpdateFailure(key, "/api/questions/" + this.props.id, status, err); }.bind(this)
	});
},

onSelect: function(eventKey){

    if ( eventKey == this.state.questionType ) return;

    this.update("questionType", eventKey);
},
onTextChosen: function(key, value) {

    var field = this.state.question.answers.splice(1, this.state.question.answers.length - 1);
    this.setState({question: this.state.question});

},
onUpdateSuccess: function(key, value) {

    this.setState(kv( key, value));
    this.state.question[key] = value;

},
onChange: function(key, event) {
    this.setState(kv(key, event.target.value));
},
onBlur: function(key) {

    if ( this.state.question[key] == this.state[key]) return;

    if ( this.state.question[key] == this.state[key].trim() ){
        this.setState(kv( key, this.state[key].trim() ));
        return;
    }

    this.update(key, this.state[key].trim() );
},
addAnswer: function() {
    var data = {};
	data.question = "api/question/"+ this.props.question.id;
	data.text = "answer";  //TODO

	$.ajax({ url: "/api/answers", dataType: 'json', type: 'POST', data : JSON.stringify(data) , headers : {'Accept' : 'application/json', 'Content-Type' : 'application/json'},
		success:    function(answer)            {       this.state.question.answers.push(answer); this.setState({question: this.state.question});                     }.bind(this),
		error:      function(xhr, status, err)  {     console.error(this.props.url, status, err.toString());    }.bind(this)
	});
},
update: function(answer, key, value) {

    var data = kv(key, value);

	$.ajax({ url: "/api/answers/" + answer.id, data: JSON.stringify(data), dataType: 'json', type: 'PATCH', headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
		     success: function(result)           { this.onUpdateSuccessAnswer(answer, key, value) }.bind(this),
		     error:   function(xhr, status, err) { console.error(this.props.url, status, err.toString());   }.bind(this)
	});
},
rightOnChange: function(answer, key, event) {

    if(key == "right" || key == "chosen"){
        this.update(answer, key,  event.target.checked);
    } else if (key == "text"){

        answer[key] = event.target.value;
        this.forceUpdate();

    } else {
        console.log("No handle for type: " + key);
    }

},
onUpdateSuccessAnswer: function(answer, key, value) {
    answer[key] = value;
    this.forceUpdate();

},



render: function() {

    var questionType = this.state.questionType;
    var answers = null;

    if( isDef( this.state.question) ){
        answers = this.state.question.answers.map(function(answer){

                if(questionType == 'TEXT_AREA'){
                    return (
                        <Input key={answer.id} value={answer.text} placeholder="This text area will be displayd to user. This cannot be evaluated automatically" type='textarea'/>
                    );
                }

                return (
                <div key={answer.id}>
                    <Input value={answer.text}     onChange={this.rightOnChange.bind(null, answer , "text")} onBlur={this.update.bind(null, answer , "text", answer.text)}  type='textarea' />
                    <Input checked={answer.right}  onChange={this.rightOnChange.bind(null, answer , "right")} type={questionType.toLowerCase()} name="answer" />
                    <Input checked={answer.chosen} onChange={this.rightOnChange.bind(null, answer , "chosen")} type={questionType.toLowerCase()} name="answer" />
                </div>
                );
            }, this);
    }


    return (
        <div>
             <h2>Question {this.state.question.position}</h2>
             <Input onChange={this.onChange.bind(null, "text")} onBlur={this.onBlur.bind(null, "text")} value={this.state.text} label={"Assigment:"} placeholder='Write question text' type='textarea'/>
             <Input onChange={this.onChange.bind(null, "privateText")} onBlur={this.onBlur.bind(null, "privateText")} value={this.state.privateText} label="Internal notes:" placeholder="Notes (NOT visible to candidate)" type='textarea'/>
             <DropdownButton onSelect={this.onSelect} title={this.state.questionType} bsStyle="info" >
                   <MenuItem eventKey="CHECKBOX">CHECKBOX</MenuItem>
                   <MenuItem eventKey="RADIO">RADIO</MenuItem>
                   <MenuItem eventKey="TEXT_AREA">TEXT</MenuItem>
             </DropdownButton>
             {answers}

             <Button disabled={questionType == "TEXT_AREA"} onClick={this.addAnswer} bsStyle='success'>Add answer</Button>
        </div>
    );
}
});