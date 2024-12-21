const ReactionsList = ({ reactions, title }) => {
    return (
      <>
        <h2>{title}</h2>
        {reactions.length > 0 ? (
          <ul>
            {reactions.map((reaction) => (
              <li key={reaction.id}>
                {reaction.firstName} {reaction.lastName}
              </li>
            ))}
          </ul>
        ) : (
          <p>No reactions to display</p>
        )}
      </>
    );
  };
  
  export default ReactionsList;