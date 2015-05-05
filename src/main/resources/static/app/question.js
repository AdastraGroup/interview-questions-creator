var Question = React.createClass({

getInitialState: function() {
	return {    question:       this.props.question,
                text:           this.props.question.text,
                privateText:    this.props.question.privateText,
                questionType:   this.props.question.questionType};
},

onSelect: function(eventKey){

    var message = "All answers for this question will be replace with one and only one text answer. Are you sure?";

    if(this.state.questionType == eventKey) return;

    if(eventKey == "TEXT_AREA"){

        if( this.state.question.answers.length > 1 && ! confirm(message)) {
            return;
        }
        $.ajax({ url: "/api/questions/" + this.props.question.id +"/textAnswer", dataType: 'json', type: 'POST', headers : {'Accept' : 'application/json', 'Content-Type' : 'application/json'},
        		success: function(answer)           { this.state.question.answers = [];
        		                                      this.state.question.answers.push(answer);
        		                                      this.state.questionType = "TEXT_AREA";
        		                                      this.setState({question: this.state.question});        }.bind(this),

        		error:   function(xhr, status, err) { console.error(this.props.url, status, err.toString()); }.bind(this)
        });

    } else {
        patchUpdate.call(this, "questions", this.props.question.id, "questionType", eventKey, this.state.questionType);
    }

},
onUpdateSuccess(id, entityUrl, key, newVal){
    this.setState(kv(key, newVal));
    this.state.question[key] = newVal;
},
onUpdateFailure(id, entityUrl, key, newVal, oldVal){
    this.setState(kv(key, oldVal));
},

onChange: function(key, event) {
    this.setState(kv(key, event.target.value));
},
addAnswer: function() {
    var data = {};
	data.question = "api/question/"+ this.props.question.id;
	data.text = "";

	$.ajax({ url: "/api/answers", dataType: 'json', type: 'POST', data : JSON.stringify(data) , headers : {'Accept' : 'application/json', 'Content-Type' : 'application/json'},
		success:    function(answer)            {       this.state.question.answers.push(answer); this.setState({question: this.state.question});                     }.bind(this),
		error:      function(xhr, status, err)  {     console.error(this.props.url, status, err.toString());    }.bind(this)
	});
},
render: function() {

    var id = this.props.question.id;
    var url = "questions";
    var questionType = this.state.questionType;
    var answers = null;


    if( isDef( this.state.question.answers) )
    {
        answers = this.state.question.answers.map(function(answer){

            return( <Answer key={answer.id} answer={answer} questionType={this.state.questionType} /> );

        }, this);
    }


    return (
        <div>
             <h2>Question {this.state.question.position}</h2>
             <Input value={this.state.text}        onChange={this.onChange.bind(null, "text")}         onBlur={patchUpdate.bind(this,url,id, "text", this.state.text, this.state.question.text)}  label="Assigment:" placeholder='Write question text' type='textarea'/>
             <Input value={this.state.privateText} onChange={this.onChange.bind(null, "privateText")}  onBlur={patchUpdate.bind(this,url,id, "privateText", this.state.privateText, this.state.question.privateText)} label="Internal notes:" placeholder="Notes (NOT visible to candidate)" type='textarea'/>
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