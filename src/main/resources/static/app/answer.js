var Answer = React.createClass({

getInitialState: function() {
	return {    answer:         this.props.answer,
                text:           this.props.answer.text,
                right:          this.props.answer.right,
                chosen:         this.props.answer.chosen,
                textUpdateOk:   false,
                textUpdateKo:   false
           };
},
onChange: function(key, value) {
    this.setState(kv(key, event.target.value));
},

onUpdateSuccess(id, entityUrl, key, newVal){
    this.setState(kv(key, newVal));                              // next 2 lines should be one call of set state
    if(key == "text") this.setState({textUpdateOk: true});
    this.state.answer[key] = newVal;
},
onUpdateFailure(id, entityUrl, key, newVal, oldVal){
    this.setState(kv(key, oldVal));                             // next 2 lines should be one call of set state
    if(key == "text") this.setState({textUpdateKo: true});
},

render: function() {

        var url = "answers";
        var id = this.state.answer.id;

        if(this.props.questionType == 'TEXT_AREA'){
            return (
                <Input value={this.state.text} onChange={this.onChange.bind(null, "text")} onBlur={patchUpdate.bind(this,url,id, "text", this.state.text, this.state.answer.text)} placeholder="Place for candidate text anser.'\n'You can write hint or anything here for candidate.'\n'Note that text answers cannot be evaulated automatically." type='textarea'/>
            );
        }

        return (
            <div>
                <Input value=  {this.state.text}   onChange={this.onChange.bind(null, "text")} onBlur={patchUpdate.bind(this,url,id, "text", this.state.text, this.state.answer.text)}  type='textarea' />
                {this.state.textUpdateOk ? <Alert bsStyle='success' onDismiss={function(){this.setState({textUpdateOk: false})}.bind(this)} dismissAfter={2000}>Saved</Alert> : null}
                {this.state.textUpdateKo ? <Alert bsStyle='danger' onDismiss={function(){this.setState({textUpdateKo: false})}.bind(this)} dismissAfter={2000}>Not Saved, try again please</Alert> : null}

                <Input label="right"  checked={this.state.right}  onChange={patchUpdate.bind(this, url, id,  "right",  !this.state.right, this.state.answer.right)}  type={this.props.questionType.toLowerCase()} name="answer" />
                <Input label="chosen" checked={this.state.chosen} onChange={patchUpdate.bind(this, url, id, "chosen", !this.state.chosen, this.state.answer.chosen)} type={this.props.questionType.toLowerCase()} name="answer" />

            </div>
        );
    }

});