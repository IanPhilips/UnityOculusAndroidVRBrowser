using System;
using System.Collections.Generic;
using UnityEditor.Collaboration;
using UnityEngine.Experimental.UIElements;

namespace UnityEditor.Collaboration
{
    internal interface ICollabHistoryItemFactory
    {
        IEnumerable<RevisionData> GenerateElements(IEnumerable<Revision> revsRevisions, int mTotalRevisions, int startIndex, string tipRev, string inProgressRevision, bool revisionActionsEnabled, bool buildServiceEnabled, string currentUser);
    }
}
