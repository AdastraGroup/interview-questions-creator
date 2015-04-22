var Interview = React.createClass({

getInitialState: function() {
	return {interview: {}, name: "", description: "", evaluationDescription: ""};
},
load: function() {
	$.ajax({    url: "/api/interviews/" + this.props.id, dataType: 'json',
	            success: function(interview) {
	                this.setState({interview: interview, name: interview.name, description: interview.description, evaluationDescription: interview.evaluationDescription});

	                 } .bind(this),
	            error: function(xhr, status, err) { console.error("/api/interviews/" + this.props.id, status, err.toString());}.bind(this)
	});
},
update: function(key, value) {

    var data = kv(key, value);

	$.ajax({ url: "/api/interviews/" + this.props.id, data: JSON.stringify(data), dataType: 'json', type: 'PATCH', headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
		     success: function(result)           { this.onUpdateSuccess(key, value); }.bind(this),
		     error:   function(xhr, status, err) { this.onUpdateFailure(key, "/api/interviews/" + this.props.id, status, err); }.bind(this)
	});
},
componentDidMount: function() {
    this.load();
},
onUpdateSuccess: function(key, value) {

    this.setState(kv( key, value));
    this.state.question[key] = value;

},
onChange: function(key, event) {
    this.setState(kv(key, event.target.value));
},
onBlur: function(key) {

    if ( this.state.interview[key] == this.state[key]) return;

    if ( this.state.interview[key] == this.state[key].trim() ){
        this.setState(kv( key, this.state[key].trim() ));
        return;
    }

    this.update(key, this.state[key].trim() );
},
onUpdateFailure: function(key, url, status, err) {
    console.error(url, status, err.toString());

    var a = {};
    a[key] = this.state.interview[key].trim();
    this.setState(a);

},
addQuestion: function() {
    var data = {};
	data.interview = "api/interview/"+ this.props.id;
	data.text = "";
	data.position = this.state.interview._embedded.questions.length + 1;

	$.ajax({ url: "/api/questions", dataType: 'json', type: 'POST', data : JSON.stringify(data) , headers : {'Accept' : 'application/json', 'Content-Type' : 'application/json'},
		success:    function(question)          {    this.state.interview._embedded.questions.push(question); this.forceUpdate();   }.bind(this),
		error:      function(xhr, status, err)  {                                       }.bind(this)
	});
},
render: function() {

    var questions = null;

        if( isDef(this.state.interview._embedded) && isDef(this.state.interview._embedded.questions)  )
        {
            questions = this.state.interview._embedded.questions.map(function(question){
                return(
                    <Question key={question.id} question={question} />
                );
            });
        }

    return (
    <div>
        <Input value={this.state.name} onChange={this.onChange.bind(null, "name")} onBlur={this.onBlur.bind(null, "name")} hasFeedback placeholder='Write interview name' label='Interview name:' type='text' />
        <Input value={this.state.description} onChange={this.onChange.bind(null, 'description')} onBlur={this.onBlur.bind(null, "description")} rows="5" label='Interview description:' placeholder='Write interview description' type='textarea'/>
        <Input value={this.state.evaluationDescription} onChange={this.onChange.bind(null, 'evaluationDescription')} onBlur={this.onBlur.bind(null, "evaluationDescription")} rows="5" label='Interview instructions:' placeholder='Write interview instructions for candidate' type='textarea'/>

        {questions}

        <Button onClick={this.addQuestion} bsStyle='primary'>Add question</Button>

    </div>
    );
}
});


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

    if(eventKey == this.state.questionType) return;

    this.update("questionType", eventKey);
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

render: function() {

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
        </div>
    );
}

});
